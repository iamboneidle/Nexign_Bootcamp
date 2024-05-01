package crm.crm_service.CRMUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой информацию для добавления нового пользователя в CDR.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataToAddNewUserToCDR {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "DataToAddNewUserToCDR{" + "msisdn='" + msisdn + '\'' + '}';
    }
}
