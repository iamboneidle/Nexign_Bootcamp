package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToPutMoney;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Services.BRTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class PutMoneyOnAccountsController {
    @Autowired
    MsisdnsRepository msisdnsRepository;
    private static final Logger LOGGER = Logger.getLogger(PutMoneyOnAccountsController.class.getName());

    @PostMapping("/put-money-on-accounts")
    @Transactional
    public ResponseEntity<String> putMoney(@RequestBody DataToPutMoney dataToPutMoney) {
        if (dataToPutMoney != null) {
            msisdnsRepository.putMoneyByNumber(dataToPutMoney.getMsisdn(), dataToPutMoney.getMoneyToPut());
            LOGGER.log(Level.INFO, "OK: " + dataToPutMoney.getMsisdn() + " got " + dataToPutMoney.getMoneyToPut() + "\n");
            return ResponseEntity.ok().body("BRT has put " + dataToPutMoney.getMoneyToPut() + " on " +
                    dataToPutMoney.getMsisdn() + " account");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data, can't put money");
        return ResponseEntity.badRequest().body("BRT got empty data, can't put money on accounts");
    }
}
