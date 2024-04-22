package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.CallReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallReceiptController {
    @PostMapping("/catch-call-receipt")
    public ResponseEntity<String> catchCallReceipt(@RequestBody CallReceipt callReceipt) {
        System.out.println(callReceipt.toString());
        return ResponseEntity.ok("good");
    }
}
