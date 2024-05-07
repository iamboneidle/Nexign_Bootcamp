package brt.brt_service.BRTUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

/**
 * Класс, представляющий собой данные из чека.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CallReceipt {
    /**
     * Номер телефона обслуживаемого абонента.
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
