package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CallRecord {
    private String callType;
    private String servicedMsisdnNumber;
    private long callTimeStart;
    private long callTimeEnd;
    private long rateId;
    private boolean isOtherMsisdnServiced;
    private Long minutesLeft;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallRecord{");
        sb.append("callType='").append(callType).append('\'');
        sb.append(", servicedMsisdnNumber='").append(servicedMsisdnNumber).append('\'');
        sb.append(", callTimeStart=").append(callTimeStart);
        sb.append(", callTimeEnd=").append(callTimeEnd);
        sb.append(", rateId=").append(rateId);
        sb.append(", isOtherMsisdnServiced=").append(isOtherMsisdnServiced);
        sb.append(", minutesLeft=").append(minutesLeft);
        sb.append('}');
        return sb.toString();
    }
}
