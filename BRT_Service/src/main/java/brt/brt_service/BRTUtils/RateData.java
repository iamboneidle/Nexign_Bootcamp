package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой данные о тарифе.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateData {
    /**
     * ID тарифа.
     */
    private Long id;
    /**
     * Название тарифа.
     */
    private String rateName;
    /**
     * Стартовая цена за месяц пользования.
     */
    private Float startCost;
    /**
     * Лимит минут.
     */
    private Long minLimit;
    /**
     * Стоимость исходящих звонков обслуживаемому абоненту.
     */
    private Float outcomingCallsCostServiced;
    /**
     * Стоимость исходящих звонков не обслуживаемому абоненту.
     */
    private Float outcomingCallsCostOthers;
    /**
     * Стоимость входящий звонков обслуживаемому абоненту.
     */
    private Float incomingCallsCostServiced;
    /**
     * Стоимость входящий звонков не обслуживаемому абоненту.
     */
    private Float incomingCallsCostOthers;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "RateData{" + "id=" + id +
                ", rateName='" + rateName + '\'' +
                ", startCost=" + startCost +
                ", minLimit=" + minLimit +
                ", outcomingCallsCostServiced=" + outcomingCallsCostServiced +
                ", outcomingCallsCostOthers=" + outcomingCallsCostOthers +
                ", incomingCallsCostServiced=" + incomingCallsCostServiced +
                ", incomingCallsCostOthers=" + incomingCallsCostOthers +
                '}';
    }
}
