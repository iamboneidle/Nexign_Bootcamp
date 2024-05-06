package hrs.hrs_service.Controllers;

import hrs.hrs_service.HRSUtils.Calculator;
import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.Services.HRSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллер, принимающий на вход информацию о звонке, чтобы далее произвести расчет.
 */
@RestController
public class DataToPayController {
    /**
     * HRSService, производящий расчет.
     */
    @Autowired
    HRSService hrsService;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(DataToPayController.class.getName());

    /**
     * Сам контроллер, который на вход принимает запрос и мапит его в объект DataToPay.
     * Затем создается новый поток, в котором производится расчет по звонку, и возвращается положительный ответ.
     * Все это происходит, если DataToPay не null.
     *
     * @param dataToPay Объект в который мапится RequestBody.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/post-data-to-pay")
    public ResponseEntity<String> catchDataToPay(@RequestBody DataToPay dataToPay) {
        if (dataToPay.getServicedMsisdnNumber() != null) {
            Thread thread = new Thread(new Calculator(hrsService, dataToPay));
            thread.start();
            LOGGER.log(Level.INFO, "OK: info for " + dataToPay.getServicedMsisdnNumber() + " was accepted");
            return ResponseEntity.ok().body("HRS accepted call data for " + dataToPay.getServicedMsisdnNumber() + " successfully");
        } else {
            LOGGER.log(Level.SEVERE, "ERROR: got request with empty data");
            return ResponseEntity.badRequest().body("HRS got empty data");
        }
    }
}
