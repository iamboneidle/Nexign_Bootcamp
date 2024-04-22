package brt.brt_service.BRTUtils;

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

    @Override
    public String toString() {
        return "CallReceipt{" + "servicedMsisdnNumber='" + servicedMsisdnNumber + '\'' +
                ", minutesToWriteOff=" + minutesToWriteOff +
                ", moneyToWriteOff=" + moneyToWriteOff +
                '}';
    }
}
