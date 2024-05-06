package cdr.cdr_service.CDRUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.StringJoiner;

/**
 * Класс представляющий транзакцию между объектами.
 */
@Getter
@Setter
@AllArgsConstructor
public class TransactionObject {
    /**
     * Тип звонка абонента
     * <br> "01" - исходящий вызов,
     * <br> "02" - входящий вызов.
     */
    private final String callType;
    /**
     * Номер телефона обслуживаемого абонента.
     */
    private final String servicedMsisdnPhoneNumber;
    /**
     * Номер телефона вызываемого абонента.
     */
    private final String contactedMsisdnPhoneNumber;
    /**
     * Время начала звонка (Unix time seconds).
     */
    private final long callStartTime;
    /**
     * Время окончания звонка (Unix time seconds).
     */
    private final long callEndTime;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта с разделителем ",".
     */
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(",");
        return result
                .add(callType)
                .add(servicedMsisdnPhoneNumber)
                .add(contactedMsisdnPhoneNumber)
                .add(Long.toString(callStartTime))
                .add(Long.toString(callEndTime)).toString();
    }
}
