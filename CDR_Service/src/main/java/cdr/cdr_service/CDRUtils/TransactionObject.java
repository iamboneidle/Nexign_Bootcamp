package cdr.cdr_service.CDRUtils;

import lombok.Getter;

import java.util.StringJoiner;

/**
 * Класс представляющий транзакцию между объектами.
 */
@Getter
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
     * Конструктор.
     *
     * @param callType                   Тип звонка абонента
     * @param servicedMsisdnPhoneNumber  Номер телефона обслуживаемого абонента.
     * @param contactedMsisdnPhoneNumber Номер телефона вызываемого абонента.
     * @param callStartTime              Время начала звонка (Unix time seconds).
     * @param callEndTime                Время окончания звонка (Unix time seconds).
     */
    TransactionObject(String callType, String servicedMsisdnPhoneNumber, String contactedMsisdnPhoneNumber, long callStartTime, long callEndTime) {
        this.callType = callType;
        this.servicedMsisdnPhoneNumber = servicedMsisdnPhoneNumber;
        this.contactedMsisdnPhoneNumber = contactedMsisdnPhoneNumber;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
    }

    /**
     * Перегруженный метод toString().
     * @return Строку вида "типЗвонка,номерОбсАбонента,номерКонтактАбонента,времяНачЗвонка,ВремяКонцЗвонка"
     */
    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(",");
        result
                .add(callType)
                .add(servicedMsisdnPhoneNumber)
                .add(contactedMsisdnPhoneNumber)
                .add(Long.toString(callStartTime))
                .add(Long.toString(callEndTime));
        return result.toString();
    }
}
