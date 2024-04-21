package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CallRecord {
    private String servicedMsisdnNumber;
    private String callType;
    private long callTimeStart;
    private long callTimeEnd;
    private long rateId;

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
