package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Services.BRTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CallReceiptController {
    @Autowired
    private BRTService brtService;
    private static final Logger LOGGER = Logger.getLogger(CallReceiptController.class.getName());

    @PostMapping("/catch-call-receipt")
    public ResponseEntity<String> catchCallReceipt(@RequestBody CallReceipt callReceipt) {
        if (callReceipt != null) {
            brtService.handleCallReceipt(callReceipt);
            LOGGER.log(Level.INFO, "OK: call receipt for " + callReceipt.getServicedMsisdnNumber() + " accepted\n");
            return ResponseEntity.ok().body("BRT accepted call receipt for " + callReceipt.getServicedMsisdnNumber() + " successfully");
        }
        LOGGER.log(Level.INFO, "ERROR: got empty call receipt\n");
        return ResponseEntity.badRequest().body("BRT got empty call receipt");
    }
}
