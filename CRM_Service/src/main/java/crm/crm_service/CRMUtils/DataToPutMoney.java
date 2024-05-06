package crm.crm_service.CRMUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для пополнения баланса на счете абонента.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DataToPutMoney {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;
    /**
     * Количество денег, которые вносятся на счет абонента.
     */
    private Float money;
}
