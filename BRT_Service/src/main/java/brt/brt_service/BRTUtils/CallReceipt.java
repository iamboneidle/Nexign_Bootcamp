package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий собой данные из чека.
 */
@AllArgsConstructor
@Getter
@Setter
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

    /**
     * Перегруженный метод toString().
     *
     * @return Строку объекта.
     */
    @Override
    public String toString() {
        return "CallReceipt{" + "servicedMsisdnNumber='" + servicedMsisdnNumber + '\'' +
                ", minutesToWriteOff=" + minutesToWriteOff +
                ", moneyToWriteOff=" + moneyToWriteOff +
                '}';
    }
}
