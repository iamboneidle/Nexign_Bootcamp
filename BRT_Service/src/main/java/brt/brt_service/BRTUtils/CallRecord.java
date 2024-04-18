package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CallRecord {
    private final String servicedMsisdnNumber;
    private final String callType;
    private final long callTimeStart;
    private final long callTimeEnd;
    private final long rateId;

    @Override
    public String toString() {
        return "{" + "servicedMsisdnNumber:" + servicedMsisdnNumber +
                ", callType:" + callType +
                ", callTimeStart:" + callTimeStart +
                ", callTimeEnd:" + callTimeEnd +
                ", rateId:" + rateId +
                '}';
    }
}
