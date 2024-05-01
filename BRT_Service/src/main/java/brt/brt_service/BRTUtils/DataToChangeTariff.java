package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой данные для изменения тарифа.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataToChangeTariff {
    /**
     * Номе телефона абонента.
     */
    private String msisdn;
    /**
     * ID тарифа.
     */
    private Long tariffId;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "DataToChangeRate{" + "msisdn='" + msisdn + '\'' +
                ", tariffId=" + tariffId +
                '}';
    }
}
