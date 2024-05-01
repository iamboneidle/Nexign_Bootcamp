package crm.crm_service.CRMUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой данные для пополнения баланса на счете абонента.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataToPutMoney {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;
    /**
     * Количество денег, которые вносятся на счет абонента.
     */
    private Float money;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "DataToPutMoney{" + "msisdn='" + msisdn + '\'' +
                ", moneyToPut=" + money +
                '}';
    }
}
