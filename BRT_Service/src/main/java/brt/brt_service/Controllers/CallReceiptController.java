package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Services.BRTService;
import brt.brt_service.Services.CallReceiptHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallReceiptController {
    @Autowired
    private BRTService brtService;
    @PostMapping("/catch-call-receipt")
    public ResponseEntity<String> catchCallReceipt(@RequestBody CallReceipt callReceipt) {
        brtService.handleCallReceipt(callReceipt);
        System.out.println(callReceipt.toString());
        return ResponseEntity.ok("good");
    }
}
