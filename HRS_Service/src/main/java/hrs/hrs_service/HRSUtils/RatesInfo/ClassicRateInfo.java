package hrs.hrs_service.HRSUtils.RatesInfo;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ClassicRateInfo {
    private final float INCOMING_FROM_OTHERS = 0;
    private final float INCOMING_FROM_SERVICED = 0;
    private final float OUTCOMING_TO_OTHERS = 2.5f;
    private final float OUTCOMING_TO_SERVICED = 1.5f;
}
