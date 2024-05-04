package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий собой данные о совершенном звонке.
 */
@AllArgsConstructor
@Getter
@Setter
public class CallRecord {
    /**
     * Тип звонка.
     */
    private String callType;
    /**
     * Номер обслуживаемого абонента.
     */
    private String servicedMsisdnNumber;
    /**
     * Время начала звонка.
     */
    private long callTimeStart;
    /**
     * Время окончания звонка.
     */
    private long callTimeEnd;
    /**
     * ID тарифа.
     */
    private long rateId;
    /**
     * Переменная отображающая информацию, является ли абонент, которому звонили обслуживаемым сервисом.
     */
    private boolean isOtherMsisdnServiced;
    /**
     * Количество оставшихся минут у абонента.
     */
    private Long minutesLeft;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "CallRecord{" + "callType='" + callType + '\'' +
                ", servicedMsisdnNumber='" + servicedMsisdnNumber + '\'' +
                ", callTimeStart=" + callTimeStart +
                ", callTimeEnd=" + callTimeEnd +
                ", rateId=" + rateId +
                ", isOtherMsisdnServiced=" + isOtherMsisdnServiced +
                ", minutesLeft=" + minutesLeft +
                '}';
    }
}
