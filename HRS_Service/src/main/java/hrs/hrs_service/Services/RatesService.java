package hrs.hrs_service.Services;

import hrs.hrs_service.DAO.Models.Rates;
import hrs.hrs_service.DAO.Repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для сущности Rates.
 */
@Service
public class RatesService {
    /**
     * Репозиторий для сущности Rates в Redis.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * ID классического тарифа.
     */
    private static final String CLASSIC_RATE_ID = "11";
    /**
     * ID помесячного тарифа.
     */
    private static final String MONTHLY_RATE_ID = "12";

    /**
     * Метод, возвращающий классический тариф из Redis.
     *
     * @return Классический тариф
     */
    public Rates getClassicRate() {
        return ratesRepository.findById(CLASSIC_RATE_ID).get();
    }

    /**
     * Метод, возвращающий помесячный тариф из Redis.
     *
     * @return Помесячный тариф
     */
    public Rates getMonthlyRate() {
        return ratesRepository.findById(MONTHLY_RATE_ID).get();
    }
}
