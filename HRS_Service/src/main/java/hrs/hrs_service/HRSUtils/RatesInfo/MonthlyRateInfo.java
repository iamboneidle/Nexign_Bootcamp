package hrs.hrs_service.HRSUtils.RatesInfo;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Информация о помесячном тарифе.
 */
//TODO: брать информацию из кэш БД
@Component
@Getter
public class MonthlyRateInfo {
    private final int MINUTES_BY_DEFAULT = 50;
    private final Float PRICE_FOR_MONTH = 100f;
}
