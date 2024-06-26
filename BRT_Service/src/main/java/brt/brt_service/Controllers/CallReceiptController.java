package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Services.BRTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллера, принимающий на вход чек по звонку.
 */
@RestController
public class CallReceiptController {
    /**
     * BRTService.
     */
    @Autowired
    private BRTService brtService;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(CallReceiptController.class.getName());

    /**
     * Контроллер, принимающий на вход данные чека по звонку.
     *
     * @param callReceipt Объект, в который мапится RequestBody.
     * @return ResponseEntity с информацией о статусе ответа.
     */
    @PostMapping("/post-call-receipt")
    public ResponseEntity<String> catchCallReceipt(@RequestBody CallReceipt callReceipt) {
        if (callReceipt.getServicedMsisdnNumber() != null) {
            brtService.handleCallReceipt(callReceipt);
            LOGGER.log(Level.INFO, "OK: call receipt for " + callReceipt.getServicedMsisdnNumber() + " accepted");
            return ResponseEntity.ok().body("BRT accepted call receipt for " + callReceipt.getServicedMsisdnNumber() + " successfully");
        }
        LOGGER.log(Level.INFO, "ERROR: got empty msisdn");
        return ResponseEntity.badRequest().body("BRT got empty msisdn");
    }
}
