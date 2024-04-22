package hrs.hrs_service.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.HRSUtils.ReceiptMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HRSService {
    @Autowired
    CallReceiptSenderService callReceiptSenderService;
    @Autowired
    ReceiptMaker receiptMaker;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void makeAndSendCallReceipt(DataToPay dataToPay) {
        try {
            String json = objectMapper.writeValueAsString(receiptMaker.makeCalculation(dataToPay));
            callReceiptSenderService.sendCallReceipt(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
