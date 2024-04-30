package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataToPutMoney {
    private String msisdn;
    private Float money;

    @Override
    public String toString() {
        return "DataToPutMoney{" + "msisdn='" + msisdn + '\'' +
                ", moneyToPut=" + money +
                '}';
    }
}
