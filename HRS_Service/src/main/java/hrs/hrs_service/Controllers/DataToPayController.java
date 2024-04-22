package hrs.hrs_service.Controllers;

import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.Services.HRSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class DataToPayController {
    @Autowired
    HRSService hrsService;
    private static final Logger LOGGER = Logger.getLogger(DataToPayController.class.getName());
    @PostMapping("/data-to-pay")
    public ResponseEntity<String> catchDataToPay(@RequestBody DataToPay dataToPay) {

        if (dataToPay != null) {
            hrsService.makeAndSendCallReceipt(dataToPay);
            LOGGER.log(Level.INFO, "OK: info for " + dataToPay.getServicedMsisdnNumber() + " was accepted" + "\n");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    "Call data for " +
                            dataToPay.getServicedMsisdnNumber() +
                            " was accepted successfully"
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("empty data");
        }
    }
}
