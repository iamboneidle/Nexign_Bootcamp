package brt.brt_service.BRTUtils;

import lombok.*;

/**
 * Класс, представляющий собой информацию для добавления нового пользователя.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataToAddNewUser {
    /**
     * Номер нового абонента.
     */
    private String msisdn;
    /**
     * ID тарифа.
     */
    private Long tariffId;
    /**
     * Количество денег, по умолчанию 100 у.е.
     */
    private Float money = 100F;
    /**
     * Имя.
     */
    private String name;
    /**
     * Фамилия.
     */
    private String surname;
    /**
     * Отчество.
     */
    private String patronymic;
}


