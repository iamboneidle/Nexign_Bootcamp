package hrs.hrs_service.HRSUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, представляющий собой чек для оплаты звонка.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CallReceipt {
    /**
     * Номер абонента.
     */
    private String servicedMsisdnNumber;
    /**
     * Минуты, которые нужно списать.
     */
    private Long minutesToWriteOff;
    /**
     * Деньги, которые нужно списать.
     */
    private Float moneyToWriteOff;
}
