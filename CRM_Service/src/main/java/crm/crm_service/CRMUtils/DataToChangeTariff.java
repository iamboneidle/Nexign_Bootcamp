package crm.crm_service.CRMUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для изменения тарифа абонента.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataToChangeTariff {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;
    /**
     * ID тарифа.
     */
    private Long tariffId;
}
