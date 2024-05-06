package cdr.cdr_service.CDRUtils;

import lombok.*;

/**
 * Этот класс используется на контроллере, на который поступает запрос из CRM на добавление нового пользователя.
 * В объект этого класса мапится RequestBody.
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
