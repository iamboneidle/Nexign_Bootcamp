package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallRecord;
import brt.brt_service.DAO.Models.CallDataRecords;
import brt.brt_service.DAO.Models.Calls;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Repository.CallDataRecordsRepository;
import brt.brt_service.DAO.Repository.CallsRepository;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service
public class CDRFileHandlerService {
    @Autowired
    MsisdnsRepository msisdnsRepository;
    @Autowired
    CallsRepository callsRepository;
    @Autowired
    CallDataRecordsRepository callDataRecordsRepository;
    @Autowired
    private MsisdnsService msisdnsService;
    @Autowired
    private DataToPaySenderService dataToPaySender;
    private int curMonth = 1;
    private int curYear = 2024;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void makeCallRecords(String cdrFile) {
        CallDataRecords cdr = new CallDataRecords(Instant.now().getEpochSecond());
        callDataRecordsRepository.save(cdr);
        String[] calls = cdrFile.split("\n");
        List<Msisdns> msisdnsList = msisdnsService.getMsisdns();
        List<String> msisdnsPhoneNumbers = msisdnsList.stream().map(Msisdns::getNumber).toList();

        for (String call: calls) {
            String[] data = call.split(",");
            String callType = data[0];
            String calledMsisdn = data[1];
            String contactedMsisdn = data[2];
            long callTimeStart = Long.parseLong(data[3]);
            long callTimeEnd = Long.parseLong(data[4]);
            if (msisdnsPhoneNumbers.contains(calledMsisdn)) {
                CallRecord callRecord = new CallRecord(
                        callType,
                        calledMsisdn,
                        callTimeStart,
                        callTimeEnd,
                        msisdnsService.getRateIdByPhoneNumber(calledMsisdn),
                        msisdnsPhoneNumbers.contains(contactedMsisdn),
                        50L
                );
                validateDate(callRecord, msisdnsList);
                send(callRecord);
                saveCallsInfo(msisdnsList, cdr, calledMsisdn, contactedMsisdn, callTimeStart, callTimeEnd);
            }
        }
    }

    private void saveCDRFiles(String cdrFile, String fileName) {

    }

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

    private void validateDate(CallRecord callRecord, List<Msisdns> msisdnsList) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(callRecord.getCallTimeStart()), ZoneId.systemDefault());

        if (dateTime.getMonthValue() > curMonth) {
            curMonth++;
            putMoneyOnAccounts();
            changeRates(msisdnsList);
        } else if (curMonth == 12 && dateTime.getMonthValue() == 1 && dateTime.getYear() > curYear) {
            curMonth = 1;
            curYear++;
            putMoneyOnAccounts();
            changeRates(msisdnsList);
        }
    }

    private void putMoneyOnAccounts() {
        float moneyToPut = (float) (Math.random() * 100 + 100);
        msisdnsRepository.increaseAllBalances(moneyToPut);
    }

    private void changeRates(List<Msisdns> msisdnsList) {
        int usersToChangeRateQuantity = (int) (Math.random() * 3);
        Collections.shuffle(msisdnsList);
        for (int i = 0; i < usersToChangeRateQuantity; i++) {
            Msisdns msisdn = msisdnsList.get(i);
            msisdnsRepository.updateRateIdByNumber(msisdn.getNumber(), (long) (msisdn.getRateId() == 11 ? 12 : 11));
        }
    }

    private void send(CallRecord callRecord) {
        try {
            String json = objectMapper.writeValueAsString(callRecord);
            dataToPaySender.sendDataToPay(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
