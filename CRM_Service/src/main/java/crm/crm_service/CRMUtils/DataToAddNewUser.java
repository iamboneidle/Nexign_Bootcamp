package crm.crm_service.CRMUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для добавления нового пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}
