package hrs.hrs_service.Controllers;

import hrs.hrs_service.DAO.Models.Rates;
import hrs.hrs_service.DAO.Repository.RatesRepository;
import hrs.hrs_service.HRSUtils.RateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллера, принимающего данные о тарифах при старте всех сервисах.
 */
@RestController
public class RateDataController {
    /**
     * Репозиторий для сущности Rates в Redis.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(RateDataController.class.getName());

    /**
     * Контроллер, принимающий данные о тарифах на старте сервисов и сохраняющий их в Redis.
     *
     * @param rateData Объект, в который мапится RequestBody.
     * @return ResponseEntity со статусом.
     */
    @PostMapping("/post-rate-data")
    public ResponseEntity<String> catchRateData(@RequestBody RateData rateData) {
        if (rateData.getRateName() != null) {
            Rates rate = new Rates(
                    String.valueOf(rateData.getId()),
                    rateData.getRateName(),
                    rateData.getStartCost(),
                    rateData.getMinLimit(),
                    rateData.getOutcomingCallsCostServiced(),
                    rateData.getOutcomingCallsCostOthers(),
                    rateData.getIncomingCallsCostServiced(),
                    rateData.getIncomingCallsCostOthers()
            );
            ratesRepository.save(rate);
            LOGGER.log(Level.INFO, "OK: got rate data for '" + rateData.getRateName() + "' tariff");
            return ResponseEntity.ok().body("HRS got rate data for '" + rateData.getRateName() + "' tariff successfully");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty rate data and will crash on the first request");
        return ResponseEntity.badRequest().body("HRS got empty rate data and will crash on the first request");
    }
}
