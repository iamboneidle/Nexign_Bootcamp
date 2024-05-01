package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой информацию для добавления нового пользователя.
 */
@Getter
@Setter
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
