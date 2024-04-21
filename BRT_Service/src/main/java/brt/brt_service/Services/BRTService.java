package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BRTService {
    @Autowired
    private MsisdnsService msisdnsService;
    @Autowired
    private DataToPaySenderService dataToPaySender;
    public void makeCallRecords(String cdrFile) {
        List<CallRecord> callRecords = new ArrayList<>();
        String[] calls = cdrFile.split("\n");

        for (String call: calls) {
            String[] data = call.split(",");
            callRecords.add(new CallRecord(
                    data[1],
                    data[0],
                    Long.parseLong(data[3]),
                    Long.parseLong(data[4]),
                    msisdnsService.getRateIdByPhoneNumber(data[1]))
            );
        }

        for (CallRecord callRecord : callRecords) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(callRecord);
                send(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveCDRFiles(String cdrFile, String fileName) {

    }

    private void send(String json) {
        dataToPaySender.sendDataToPay(json);
    }
}
