package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallRecord;
import brt.brt_service.DAO.Models.Msisdns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BRTService {
    @Autowired
    private MsisdnsService msisdnsService;
    @Autowired
    private DataToPaySenderService dataToPaySender;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void makeCallRecords(String cdrFile) {
        String[] calls = cdrFile.split("\n");
        List<Msisdns> msisdnsList = msisdnsService.getMsisdns();
        List<String> msisdnsPhoneNumbers = msisdnsList.stream().map(Msisdns::getNumber).toList();

        for (String call: calls) {
            String[] data = call.split(",");
            String callType = data[0];
            String msisdnPhoneNumber = data[1];
            String contactedMsisdn = data[2];
            long callTimeStart = Long.parseLong(data[3]);
            long callTimeEnd = Long.parseLong(data[4]);
            if (msisdnsPhoneNumbers.contains(msisdnPhoneNumber)) {
                send(new CallRecord(
                                callType,
                                msisdnPhoneNumber,
                                callTimeStart,
                                callTimeEnd,
                                msisdnsService.getRateIdByPhoneNumber(msisdnPhoneNumber),
                                msisdnsPhoneNumbers.contains(contactedMsisdn),
                                msisdnsList.stream().filter(msisdns -> msisdns
                                        .getNumber()
                                        .equals(msisdnPhoneNumber)).findFirst().get().getMinutesLeft()
                        )
                );
            }
        }
    }

    private void saveCDRFiles(String cdrFile, String fileName) {

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
