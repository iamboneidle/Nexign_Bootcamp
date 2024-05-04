package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToPutMoney;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллера, принимающий данные о пополнении баланса на счете абонента.
 */
@RestController
public class PutMoneyOnAccountsController {
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(PutMoneyOnAccountsController.class.getName());

    /**
     * Контроллер, принимающий информацию для пополнения баланса на счете абонента.
     *
     * @param dataToPutMoney Объект, в который мапится RequestBody.
     * @return ResponseEntity со статусом ответа.
     */
    @PostMapping("/put-money-on-accounts")
    @Transactional
    public ResponseEntity<String> putMoney(@RequestBody DataToPutMoney dataToPutMoney) {
        if (ObjectUtils.allNotNull(dataToPutMoney.getMoney(), dataToPutMoney.getMsisdn())) {
            msisdnsRepository.putMoneyByNumber(dataToPutMoney.getMsisdn(), dataToPutMoney.getMoney());
            LOGGER.log(Level.INFO, "OK: " + dataToPutMoney.getMsisdn() + " got " + dataToPutMoney.getMoney());
            return ResponseEntity.ok().body("BRT has put " + dataToPutMoney.getMoney() + " on " +
                    dataToPutMoney.getMsisdn() + " account");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data, can't put money");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("BRT got empty data, can't put money on accounts");
    }
}
