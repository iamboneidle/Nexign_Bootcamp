package crm.crm_service.Services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class CRMService {
    private final Map<String, Long> mapNumberToRateId = new HashMap<>();

    public void setMapNumberToRateId(Map<String, Integer> mapNumberToRateIdNew) {
        mapNumberToRateIdNew.forEach((k, v) -> this.mapNumberToRateId.put(k, v.longValue()));
    }
}
