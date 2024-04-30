package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataToChangeTariff {
    private String msisdn;
    private Long tariffId;

    @Override
    public String toString() {
        return "DataToChangeRate{" + "msisdn='" + msisdn + '\'' +
                ", tariffId=" + tariffId +
                '}';
    }
}
