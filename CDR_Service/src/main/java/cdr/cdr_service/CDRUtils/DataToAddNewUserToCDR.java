package cdr.cdr_service.CDRUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Этот класс используется на контроллере, на который поступает запрос из CRM на добавление нового пользователя.
 * В объект этого класса мапится RequestBody.
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
