package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToChangeTariff;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Models.Rates;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.DAO.Repository.RatesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллера, отвечающий за прием информации о смене тарифа с CRM.
 */
@RestController
public class DataToChangeTariffController {
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий тарифов.
     */
    @Autowired
    RatesRepository ratesRepository;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(DataToChangeTariffController.class.getName());

    /**
     * Контроллер, принимающий информацию о смене тарифов.
     *
     * @param dataToChangeTariff Объект, в который мапится RequestBody.
     * @return ResponseEntity со статусом ответа.
     */
    @PostMapping("/change-tariff")
    @Transactional
    public ResponseEntity<String> catchData(@RequestBody DataToChangeTariff dataToChangeTariff) {
        if (dataToChangeTariff != null) {
            Msisdns msisdnsByNumber = msisdnsRepository.findMsisdnsByNumber(dataToChangeTariff.getMsisdn());
            Rates rate = ratesRepository.findRatesById(dataToChangeTariff.getTariffId());
            msisdnsByNumber.setRates(rate);
            msisdnsRepository.save(msisdnsByNumber);
            LOGGER.log(Level.INFO, "OK: here is " + dataToChangeTariff.getMsisdn() + " new tariff id: " + dataToChangeTariff.getTariffId());
            return ResponseEntity.ok("BRT accepted new tariff info '" + dataToChangeTariff.getTariffId() + "' for " + dataToChangeTariff.getMsisdn() + " successfully");
        }
        LOGGER.log(Level.INFO, "ERROR: got empty data, can't change tariff");
        return ResponseEntity.badRequest().body("BRT got empty info from CRM about changing tariffs");
    }
}
