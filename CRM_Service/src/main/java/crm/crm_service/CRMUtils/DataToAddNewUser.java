package crm.crm_service.CRMUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataToAddNewUser {
    private String msisdn;
    private Long tariffId;
    private Float money = 100F;
    private String name;
    private String surname;
    private String patronymic;

    @Override
    public String toString() {
        return "DataToAddNewUser{" + "msisdn='" + msisdn + '\'' +
                ", tariffId=" + tariffId +
                ", money=" + money +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}
