package hrs.hrs_service.HRSUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataToPay {
    private String servicedMsisdnNumber;
    private String callType;
    private Long callTimeStart;
    private Long callTimeEnd;
    private Long rateId;

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
