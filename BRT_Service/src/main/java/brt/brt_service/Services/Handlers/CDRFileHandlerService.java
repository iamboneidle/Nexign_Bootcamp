package brt.brt_service.Services.Handlers;

import brt.brt_service.BRTUtils.CallRecord;
import brt.brt_service.BRTUtils.RequestExecutor;
import brt.brt_service.Postgres.DAO.Models.CallDataRecords;
import brt.brt_service.Postgres.DAO.Models.Calls;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Repository.CallDataRecordsRepository;
import brt.brt_service.Postgres.DAO.Repository.CallsRepository;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
import brt.brt_service.Services.Utils.MsisdnsService;
import brt.brt_service.TGBot.CDRFileStorageBot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, обрабатывающий CDR файлы.
 */
@Service
public class CDRFileHandlerService {
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий звонков.
     */
    @Autowired
    private CallsRepository callsRepository;
    /**
     * Репозиторий CDR файлов.
     */
    @Autowired
    private CallDataRecordsRepository callDataRecordsRepository;
    /**
     * Репозиторий абонентов и остатка их минут в Redis.
     */
    @Autowired
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;
    /**
     * Сервис абонентов.
     */
    @Autowired
    private MsisdnsService msisdnsService;
    /**
     * Класс, отправляющий запросы.
     */
    @Autowired
    private RequestExecutor requestExecutor;
    @Autowired
    private CDRFileStorageBot cdrFileStorageBot;
    /**
     * Настоящий месяц (сервис запускается 01.01.2024).
     */
    private int curMonth = 1;
    /**
     * Настоящий год (сервис запускается 01.01.2024).
     */
    private int curYear = 2024;
    /**
     * Объект ObjectMapper для преобразования объектов в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * URL-адрес для отправки флажка о смене тарифа на CRM.
     */
    private static final String CHANGE_TARIFF_URL = "http://localhost:2004/admin/change-tariff-monthly";
    /**
     * URL-адрес для отправки флажка о пополнении баланса на счетах всех абонентов на CRM.
     */
    private static final String PUT_MONEY_URL = "http://localhost:2004/admin/put-money-monthly";
    /**
     * URL-адрес для отправки данных по звонку на HRS.
     */
    private static final String POST_DATA_TO_PAY_URL = "http://localhost:2003/post-data-to-pay";
    /**
     * Имя пользователя админа в CRM.
     */
    private static final String ADMIN_USERNAME = "admin";
    /**
     * Пароль админа в CRM.
     */
    private static final String ADMIN_PASSWORD = "admin";
    /**
     * Путь, к папке, в которую каждый новый файл записывается.
     */
    private final Path ROOT_PATH = Paths.get("BRT_Service/src/main/resources/CDR_Files").toAbsolutePath();
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(CDRFileHandlerService.class.getName());

    /**
     * Метод, который создает объекты CallRecord из строк поступившего CDR файла.
     * Перед созданием он проверяет, является ли звонивший абонент обслуживаемым,
     * если нет, то просто его не обрабатывает. Вызывает метод, следящий за актуальной
     * датой, метод сохраняющий CDR файл, метод записывающий в БД звонки.
     *
     * @param cdrFile CDR файл.
     * @param fileName Имя CDR файла.
     */
    @Transactional
    public void makeCallRecords(String cdrFile, String fileName) {
        CallDataRecords cdr = new CallDataRecords(Instant.now().getEpochSecond());
        callDataRecordsRepository.save(cdr);
        String[] calls = cdrFile.split("\n");
        saveCDRFile(fileName, calls);
        List<Msisdns> msisdnsList = msisdnsService.getMsisdns();
        List<String> msisdnsPhoneNumbers = msisdnsList.stream().map(Msisdns::getNumber).toList();
        for (String call : calls) {
            String[] data = call.split(",");
            String callType = data[0];
            String calledMsisdn = data[1];
            Optional<MsisdnToMinutesLeft> optionalMsisdn = msisdnToMinutesLeftRepository.findById(calledMsisdn);
            if (msisdnsPhoneNumbers.contains(calledMsisdn) && optionalMsisdn.isPresent()) {
                String contactedMsisdn = data[2];
                long callTimeStart = Long.parseLong(data[3]);
                long callTimeEnd = Long.parseLong(data[4]);
                CallRecord callRecord = new CallRecord(
                        callType,
                        calledMsisdn,
                        callTimeStart,
                        callTimeEnd,
                        msisdnsService.getRateIdByPhoneNumber(calledMsisdn),
                        msisdnsPhoneNumbers.contains(contactedMsisdn),
                        optionalMsisdn.get().getMinutesLeft()
                );
                validateDate(callRecord);
                sendCallRecord(callRecord);
                saveCallsInfo(msisdnsList, cdr, calledMsisdn, contactedMsisdn, callTimeStart, callTimeEnd);
                if (callType.equals("01")) {
                    msisdnsRepository.increaseOutcomingCallsQuantity(calledMsisdn);
                } else {
                    msisdnsRepository.increaseIncomingCallsQuantity(calledMsisdn);
                }
            }
        }
    }

    /**
     * Метод, отправляющий CDR файлы в телеграм бота.
     *
     * @param file  Файл.
     * @param fileName Имя файла.
     */
    private void senCDRFileToTG(File file, String fileName) {
        cdrFileStorageBot.sendDocument(file, fileName);
    }

    /**
     * Метод, сохраняющий файлы в папку и отправляющий в телеграм бота, если ему написать
     * в начале.
     *
     * @param fileName Название файла.
     * @param calls Массив звонков.
     */
    private void saveCDRFile(String fileName, String[] calls) {
        Path filePath = Paths.get(ROOT_PATH + "/" + fileName + ".txt");
        try {
            if (!Files.exists(ROOT_PATH.toAbsolutePath())) {
                Files.createDirectory(ROOT_PATH.toAbsolutePath());
            }
            Files.deleteIfExists(filePath);
            Path file = Files.createFile(filePath);

            try (FileOutputStream outputStream = new FileOutputStream(file.toFile())) {

                for (String call : calls) {
                    outputStream.write((call + "\n").getBytes());
                    outputStream.flush();
                }
            }
            senCDRFileToTG(file.toFile(), fileName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Метод, сохраняющий информацию о звонке в базу данных.
     *
     * @param msisdnsList     Список абонентов.
     * @param cdr             Объект CDR файла.
     * @param calledMsisdn    Звонивший абонент.
     * @param contactedMsisdn Абонент, которому звонили.
     * @param callTimeStart   Время начала звонка.
     * @param callTimeEnd     Время окончания звонка.
     */
    private void saveCallsInfo(List<Msisdns> msisdnsList, CallDataRecords cdr, String calledMsisdn, String contactedMsisdn, long callTimeStart, long callTimeEnd) {
        Calls call = new Calls(
                msisdnsList.stream().filter(msisdns -> msisdns
                        .getNumber()
                        .equals(calledMsisdn)).findFirst().get(),
                msisdnsList.stream().filter(msisdns -> msisdns
                        .getNumber()
                        .equals(contactedMsisdn)).findFirst().orElse(null),
                callTimeStart,
                callTimeEnd,
                callTimeEnd - callTimeStart,
                cdr
        );
        callsRepository.save(call);
    }

    /**
     * Метод, который обновляет дату в сервисе, а также при наступлении нового месяца вызывает методы
     * putMoneyOnAccounts() и changeRates().
     *
     * @param callRecord Данные о звонке.
     */
    private void validateDate(CallRecord callRecord) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(callRecord.getCallTimeStart()), ZoneId.systemDefault());

        if (dateTime.getMonthValue() > curMonth) {
            curMonth++;
            putMinutesOnAccounts();
            putMoneyOnAccounts();
            changeRates();
        } else if (curMonth == 12 && dateTime.getMonthValue() == 1 && dateTime.getYear() > curYear) {
            curMonth = 1;
            curYear++;
            putMinutesOnAccounts();
            putMoneyOnAccounts();
            changeRates();
        }
    }

    /**
     * Метод кладет 50 минут всем абонентам помесячного тарифа.
     */
    private void putMinutesOnAccounts() {
        List<MsisdnToMinutesLeft> msisdnsToPutMinutes = new ArrayList<>();
        msisdnToMinutesLeftRepository.findAll().forEach(msisdnToMinutesLeft -> {
            if (msisdnToMinutesLeft.getMinutesLeft() != null) {
                msisdnToMinutesLeft.setMinutesLeft(50L);
                msisdnsToPutMinutes.add(msisdnToMinutesLeft);
            }
        });
        msisdnToMinutesLeftRepository.saveAll(msisdnsToPutMinutes);
    }

    /**
     * Метод, отправляющий запрос в CRM, чтобы тот положил деньги на балансы счетов всех абонентов.
     */
    private void putMoneyOnAccounts() {
        String json = "new month " + curMonth + "." + curYear + " has come";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        requestExecutor.executeWithHeaders(PUT_MONEY_URL, body, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    /**
     * Метод, запрашивающий CRM обновить у нескольких абонентов тарифы.
     */
    private void changeRates() {
        String json = "new month " + curMonth + "." + curYear + " has come";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        requestExecutor.executeWithHeaders(CHANGE_TARIFF_URL, body, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    /**
     * Метод, отправляющий данные о звонке в HRS.
     *
     * @param callRecord Объект с данными о звонке.
     */
    private void sendCallRecord(CallRecord callRecord) {
        try {
            String json = objectMapper.writeValueAsString(callRecord);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            requestExecutor.execute(POST_DATA_TO_PAY_URL, body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
