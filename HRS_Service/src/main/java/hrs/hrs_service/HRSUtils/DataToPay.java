package hrs.hrs_service.HRSUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, представляющий собой данные о звонке.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DataToPay {
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
    private Long callTimeStart;
    /**
     * Время окончания звонка.
     */
    private Long callTimeEnd;
    /**
     * Тариф.
     */
    private int rateId;
    /**
     * Переменная говорящая о том, является ли номер телефона, на который звонили, обслуживаемым.
     */
    private boolean isOtherMsisdnServiced;
    /**
     * Сколько минут осталось.
     */
    private Long minutesLeft;
    /**
     * Количество минут по умолчанию.
     */
    private Long minutesByDefault;
}
