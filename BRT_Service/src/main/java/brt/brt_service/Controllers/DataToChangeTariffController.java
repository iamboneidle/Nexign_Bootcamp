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

@RestController
public class DataToChangeTariffController {
    @Autowired
    MsisdnsRepository msisdnsRepository;
    @Autowired
    RatesRepository ratesRepository;
    @PostMapping("/change-tariff")
    @Transactional
    public ResponseEntity<String> catchData(@RequestBody DataToChangeTariff dataToChangeTariff) {
        if (dataToChangeTariff != null) {
            Msisdns msisdnsByNumber = msisdnsRepository.findMsisdnsByNumber(dataToChangeTariff.getMsisdn());
            Rates rate = ratesRepository.findRatesById(dataToChangeTariff.getTariffId());
            msisdnsByNumber.setRates(rate);
            msisdnsRepository.save(msisdnsByNumber);
            return ResponseEntity.ok("BRT accepted new tariff info for " + dataToChangeTariff.getMsisdn() + " successfully");
        }

        return ResponseEntity.badRequest().body("BRT got empty info from CRM about changing tariffs");
    }
}
