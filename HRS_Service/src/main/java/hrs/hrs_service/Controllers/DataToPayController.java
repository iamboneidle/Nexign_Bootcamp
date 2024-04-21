package hrs.hrs_service.Controllers;

import hrs.hrs_service.HRSUtils.DataToPay;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DataToPayController {
    @PostMapping("/data-to-pay")
    public ResponseEntity<String> catchDataToPay(@RequestBody DataToPay dataToPay) {
        return ResponseEntity.ok(
                "Call data for " +
                        dataToPay.getServicedMsisdnNumber() +
                        " was accepted successfully"
        );
    }
}
