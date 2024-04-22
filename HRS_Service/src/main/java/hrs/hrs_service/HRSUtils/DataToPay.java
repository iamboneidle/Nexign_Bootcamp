package hrs.hrs_service.HRSUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataToPay {
    private String servicedMsisdnNumber;
    private String callType;
    private Long callTimeStart;
    private Long callTimeEnd;
    private int rateId;
    private boolean isOtherMsisdnServiced;
    private Long minutesLeft;
    private Long minutesByDefault;

    @Override
    public String toString() {
        return "DataToPay{" + "servicedMsisdnNumber='" + servicedMsisdnNumber + '\'' +
                ", callType='" + callType + '\'' +
                ", callTimeStart=" + callTimeStart +
                ", callTimeEnd=" + callTimeEnd +
                ", rateId=" + rateId +
                '}';
    }
}
