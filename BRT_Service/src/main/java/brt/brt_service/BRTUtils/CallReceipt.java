package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
