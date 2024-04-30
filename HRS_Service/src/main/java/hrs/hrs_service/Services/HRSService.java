package hrs.hrs_service.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrs.hrs_service.HRSUtils.CallReceipt;
import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.HRSUtils.ReceiptMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class HRSService {
    @Autowired
    private CallReceiptSenderService callReceiptSenderService;
    @Autowired
    private ReceiptMaker receiptMaker;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(HRSService.class.getName());

    public void makeAndSendCallReceipt(DataToPay dataToPay) {
        try {
            CallReceipt callReceipt = receiptMaker.makeCalculation(dataToPay);
            if (callReceipt != null) {
                String json = objectMapper.writeValueAsString(callReceipt);
                callReceiptSenderService.sendCallReceipt(json);
            }
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
