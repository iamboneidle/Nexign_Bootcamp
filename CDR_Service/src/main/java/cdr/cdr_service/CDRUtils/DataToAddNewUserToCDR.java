package cdr.cdr_service.CDRUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataToAddNewUserToCDR {
    private String msisdn;

    @Override
    public String toString() {
        return "DataToAddNewUserToCDR{" + "msisdn='" + msisdn + '\'' + '}';
    }
}
