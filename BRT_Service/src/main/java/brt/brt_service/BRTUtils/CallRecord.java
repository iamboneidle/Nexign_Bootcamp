package brt.brt_service.BRTUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

/**
 * Класс, представляющий собой данные о совершенном звонке.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
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
}

