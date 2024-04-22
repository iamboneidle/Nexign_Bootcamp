package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CallRecord {
    private String servicedMsisdnNumber;
    private String callType;
    private long callTimeStart;
    private long callTimeEnd;
    private long rateId;
    private boolean isOtherMsisdnServiced;
    private Long minutesLeft;

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
