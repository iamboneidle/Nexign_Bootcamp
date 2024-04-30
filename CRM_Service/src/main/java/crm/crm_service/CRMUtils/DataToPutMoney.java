package crm.crm_service.CRMUtils;

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
    private Float moneyToPut;

    @Override
    public String toString() {
        return "DataToPutMoney{" + "msisdn='" + msisdn + '\'' +
                ", moneyToPut=" + moneyToPut +
                '}';
    }
}
