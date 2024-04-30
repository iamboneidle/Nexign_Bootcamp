package crm.crm_service.Services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CRMService {
    @Getter
    private Map<String, Long> mapNumberToRateId = new HashMap<>();

    public void setMapNumberToRateId(Map<String, Integer> mapNumberToRateIdNew) {

        mapNumberToRateIdNew.forEach((k, v) -> {
            this.mapNumberToRateId.put(k, v.longValue());
        });
    }
}
