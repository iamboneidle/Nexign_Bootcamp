package hrs.hrs_service.HRSUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CallReceipt {
    private String servicedMsisdnNumber;
    private Long minutesToWriteOff;
    private Float moneyToWriteOff;
}
