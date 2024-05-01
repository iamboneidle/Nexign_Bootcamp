package crm.crm_service.Services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис CRM.
 */
@Getter
@Service
public class CRMService {
    /**
     * Мапа номерТелефонаАбонент-->тарифАбонента.
     */
    private final Map<String, Long> mapNumberToRateId = new HashMap<>();

    /**
     * Сеттер для mapNumberToRateId.
     *
     * @param newMapNumberToRateId Мапа, которая приходит с контроллера и переделывается в нужную нам.
     */
    public void setMapNumberToRateId(Map<String, Integer> newMapNumberToRateId) {
        newMapNumberToRateId.forEach((k, v) -> this.mapNumberToRateId.put(k, v.longValue()));
    }
}
