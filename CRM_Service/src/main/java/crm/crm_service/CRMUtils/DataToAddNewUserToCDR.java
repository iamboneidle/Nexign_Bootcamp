package crm.crm_service.CRMUtils;

import lombok.*;

/**
 * Класс, представляющий собой информацию для добавления нового пользователя в CDR.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataToAddNewUserToCDR {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;
}
