package crm.crm_service.CRMUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой данные для добавления нового пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataToAddNewUser {
    /**
     * Номер телефона пользователя.
     */
    private String msisdn;
    /**
     * ID тарифа.
     */
    private Long tariffId;
    /**
     * Количество денег на счету после регистрации. (Если в запросе на CRM не передается информация по количеству
     * денег, то по умолчанию на счете будет 100 у.е.)
     */
    private Float money = 100F;
    /**
     * Имя абонента.
     */
    private String name;
    /**
     * Фамилия абонента.
     */
    private String surname;
    /**
     * Отчество абонента.
     */
    private String patronymic;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
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
