package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallRecord;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class BRTService {
    @Autowired
    MsisdnsRepository msisdnsRepository;
    @Autowired
    MsisdnsService msisdnsService;

    public void makeCallRecords(String cdrFile, String fileName) {
        System.out.println(fileName + "\n");
        List<CallRecord> callRecords = new ArrayList<>();

        String[] calls = cdrFile.split("\n");
        for (String call: calls) {
            String[] data = call.split(",");
            long rateId = msisdnsService.getRateIdByPhoneNumber(data[1]);
            callRecords.add(new CallRecord(data[1], data[0], Long.parseLong(data[3]), Long.parseLong(data[4]), rateId));
        }
        for (CallRecord callRecord : callRecords) {
            ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
            try {
                String jsonWithTabs = objectWriter.writeValueAsString(callRecord);
                System.out.println(jsonWithTabs + "\n");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveCDRFiles(String cdrFile, String fileName) {

    }
}
