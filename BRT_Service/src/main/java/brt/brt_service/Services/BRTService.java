package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.BRTUtils.RequestExecutor;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Services.Handlers.CDRFileHandlerService;
import brt.brt_service.Services.Handlers.CallReceiptHandlerService;
import brt.brt_service.Services.Utils.StartInfoPusherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BRTService {
    @Autowired
    private CDRFileHandlerService callRecordsHandlerService;
    @Autowired
    private CallReceiptHandlerService callReceiptHandlerService;
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private RequestExecutor requestExecutor;
    @Autowired
    private StartInfoPusherService startInfoPusherService;
    private static final String DESTINATION_URL = "http://localhost:2004/admin/post-tariffs";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public void handleCDRFile(String callDataRecord) {
        callRecordsHandlerService.makeCallRecords(callDataRecord);
    }

    public void handleCallReceipt(CallReceipt callReceipt) { callReceiptHandlerService.validateCallReceipt(callReceipt); }

    private void sendRatesToCacheDB_HRS() {/* TODO: @PostConstruct */ }

    @PostConstruct
    public void sendDataToCRM() throws JsonProcessingException {
        startInfoPusherService.pushToDB();
        Map<String, Long> mapNumberToRateId = new HashMap<>();
        List<Msisdns> msisdns = msisdnsRepository.findAll();
        for (Msisdns msisdn : msisdns) {
            mapNumberToRateId.put(msisdn.getNumber(), msisdn.getRateId());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(mapNumberToRateId);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        requestExecutor.executeWithHeaders(DESTINATION_URL, body, ADMIN_USERNAME, ADMIN_PASSWORD);
    }
}
