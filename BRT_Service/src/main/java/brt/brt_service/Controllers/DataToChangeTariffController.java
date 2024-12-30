package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToChangeTariff;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Models.Rates;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Postgres.DAO.Repository.RatesRepository;
import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий тарифов.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Репозиторий абонентов и остатка их минут в Redis.
     */
    @Autowired
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;
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
        if (ObjectUtils.allNotNull(dataToChangeTariff.getTariffId(), dataToChangeTariff.getMsisdn())) {
            Msisdns msisdnsByNumber = msisdnsRepository.findMsisdnsByNumber(dataToChangeTariff.getMsisdn());
            Rates rate = ratesRepository.findRatesById(dataToChangeTariff.getTariffId());
            msisdnsByNumber.setRates(rate);
            msisdnsRepository.save(msisdnsByNumber);
            MsisdnToMinutesLeft msisdnToMinutesLeft = msisdnToMinutesLeftRepository.findById(dataToChangeTariff.getMsisdn()).orElse(null);
            if (msisdnToMinutesLeft != null) {
                msisdnToMinutesLeft.setMinutesLeft(rate.getMinLimit());
                msisdnToMinutesLeftRepository.save(msisdnToMinutesLeft);
            }
            LOGGER.log(Level.INFO, "OK: here is " + dataToChangeTariff.getMsisdn() + " new tariff id: " + dataToChangeTariff.getTariffId());
            return ResponseEntity.ok("BRT accepted new tariff info '" + dataToChangeTariff.getTariffId() + "' for " + dataToChangeTariff.getMsisdn() + " successfully");
        }
        LOGGER.log(Level.INFO, "ERROR: got empty data, can't change tariff");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BRT got empty info from CRM about changing tariffs");
    }
}
