package hrs.hrs_service.HRSUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrs.hrs_service.DAO.Models.Rates;
import hrs.hrs_service.Services.CallReceiptSenderService;
import hrs.hrs_service.Services.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, отвечающий за подготовку чека по звонку.
 */
@Component
public class ReceiptMaker {
    /**
     * Сервис по отправке чеков.
     */
    @Autowired
    private CallReceiptSenderService callReceiptSenderService;
    /**
     * Сервис для сущности Rates.
     */
    @Autowired
    private RatesService ratesService;
    /**
     * Месяц, с которого начинает работать сервис. У нас он начинает работать с 01.01.2024.
     */
    private int curMonth = 1;
    /**
     * Год, с которого начинает работать сервис. У нас он начинает работать с 01.01.2024.
     */
    private int curYear = 2024;
    /**
     * Объект ObjectMapper для преобразования объектов в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Список с абонентами месячного тарифа, который заполняется и в конце каждого месяца отправляется чек
     * по каждому на списание средств со счета.
     */
    private final List<String> monthlyRateUsers = new ArrayList<>();
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(ReceiptMaker.class.getName());

    /**
     * Метод, который вызывает rateSwitch() и обновляет дату в сервисе, а при наступлении нового месяца
     * вызывает sendMonthlyRateUsersReceipts().
     *
     * @param dataToPay Информация по звонку.
     * @return Чек по звонку.
     */
    public CallReceipt makeCalculation(DataToPay dataToPay) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dataToPay.getCallTimeStart()), ZoneId.systemDefault());

        if (dateTime.getMonthValue() > curMonth) {
            curMonth++;
            sendMonthlyRateUsersReceipts();
            return rateSwitch(dataToPay);
        } else if (curMonth == 12 && dateTime.getMonthValue() == 1 && dateTime.getYear() > curYear) {
            curMonth = 1;
            curYear++;
            sendMonthlyRateUsersReceipts();
            return rateSwitch(dataToPay);
        } else {
            return rateSwitch(dataToPay);
        }
    }

    /**
     * Метод, который выбирает, по какому тарифу производить расчет.
     *
     * @param dataToPay Информация по звонку.
     * @return Чек по звонку.
     */
    private CallReceipt rateSwitch(DataToPay dataToPay) {
        return switch (dataToPay.getRateId()) {
            case 11 -> calculateByClassicRate(dataToPay);
            case 12 -> calculateByMonthlyRate(dataToPay);
            default -> {
                LOGGER.log(Level.SEVERE, "ERROR: Unexpected value:" + dataToPay.getRateId() + "\n");
                throw new IllegalStateException("Unexpected value: " + dataToPay.getRateId() + "\n");
            }
        };
    }

    /**
     * Метод, который производит расчет по классическому тарифу. Он считает продолжительность звонка,
     * переводит ее в минуты и округляет в большую сторону, затем производит расчет, смотря на то,
     * исходящий или входящий был звонок, а также звонили ли обслуживаемому абоненту или нет.
     *
     * @param dataToPay Информация по звонку.
     * @return Чек по звонку.
     */
    private CallReceipt calculateByClassicRate(DataToPay dataToPay) {
        Rates classicRate = ratesService.getClassicRate();
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getCallType().equals("02")) {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRate.getIncomingCallsCostServiced()
                            : classicRate.getIncomingCallsCostOthers()
                    )
            );
        } else {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRate.getOutcomingCallsCostServiced()
                            : classicRate.getOutcomingCallsCostOthers()
                    )
            );
        }
    }

    /**
     * Метод, который производит расчет по классическому тарифу. Он считает продолжительность звонка,
     * переводит ее в минуты и округляет в большую сторону, затем производит расчет, смотря на то,
     * исходящий или входящий был звонок, а также звонили ли обслуживаемому абоненту или нет.
     *
     * @param dataToPay Объект с информацией о звонке.
     * @param callDuration Минуты, количество которых нужно списать в кэш базе данных в BRT.
     * @return Чек по звонку.
     */
    private CallReceipt calculateByClassicRate(DataToPay dataToPay, Long callDuration) {
        Rates classicRate = ratesService.getClassicRate();
        if (dataToPay.getCallType().equals("02")) {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    callDuration,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRate.getIncomingCallsCostServiced()
                            : classicRate.getIncomingCallsCostOthers()
                    )
            );
        } else {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    callDuration,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRate.getOutcomingCallsCostServiced()
                            : classicRate.getOutcomingCallsCostOthers()
                    )
            );
        }
    }

    /**
     * Метод считает продолжительность звонка, если абонент не "перелимитил" количество своих минут, то он
     * сохраняется в список monthlyRateUsers, по нему возвращается чек в BRT с нулевой суммой списания и количеством
     * минут на списание, которое заносится в кэш базу данных BRT, этот чек BRT не обрабатывает в
     * основную базу данных. В обратном случае рассчитывается, сколько времени абонент наговорил
     * сверх лимита, это количество вычитается из времени окончания звонка и обновленный объект уходит
     * в метод расчета классического тарифа со списанием и денег и минут.
     * DataToPay
     *
     * @param dataToPay Информация по звонку.
     * @return Чек по звонку.
     */
    private CallReceipt calculateByMonthlyRate(DataToPay dataToPay) {
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getMinutesLeft() - callDuration >= 0) {
            if (!monthlyRateUsers.contains(dataToPay.getServicedMsisdnNumber())) {
                monthlyRateUsers.add(dataToPay.getServicedMsisdnNumber());
            }
            return new CallReceipt (
                    dataToPay.getServicedMsisdnNumber(),
                    callDuration,
                    0F
            );
        } else {
            long newCallDuration = callDuration - dataToPay.getMinutesLeft();
            long newCallTimeEnd = dataToPay.getCallTimeStart() + newCallDuration * 60;
            dataToPay.setCallTimeEnd(newCallTimeEnd);
            return calculateByClassicRate(dataToPay, newCallDuration);
        }
    }

    /**
     * Метод, который каждый месяц производит подготовку чека по абонентам месячного тарифа и отправляет в BRT.
     */
    private void sendMonthlyRateUsersReceipts() {
        for (String phoneNumber : monthlyRateUsers) {
            CallReceipt callReceipt = new CallReceipt(phoneNumber, null, ratesService.getMonthlyRate().getStartCost());
            try {
                String json = objectMapper.writeValueAsString(callReceipt);
                callReceiptSenderService.sendCallReceipt(json);
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
