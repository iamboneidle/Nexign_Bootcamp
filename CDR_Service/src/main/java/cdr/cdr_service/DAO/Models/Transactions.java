package cdr.cdr_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Сущность в базе данных под названием Transactions.
 * <br>Нужна для хранения информации о транзакциях каждого абонента.
 */
@Entity
@Data
@NoArgsConstructor
public class Transactions {
    /**
     * Первичный ключ транзакции.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Ссылка на объект Msisdns, связанный с транзакцией.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "msisdn_id")
    private Msisdns msisdns;

    /**
     * Первичный ключ Msisdn, связанного с данной транзакцией.
     */
    @Column(name = "msisdn_id", insertable = false, updatable = false)
    private Long msisdnId;

    /**
     * Тип вызова.
     * <br> "01" - исходящий вызов.
     * <br> "02" - входящий вызов.
     */
    @Column(name = "call_type")
    private String callType;

    /**
     * Номер телефона абонента, с которым осуществлялась связь.
     */
    @Column(name = "contacted_msisdn")
    private String contactedMsisdn;

    /**
     * Время начала звонка (Unix time seconds).
     */
    @Column(name = "call_time_start")
    private long callTimeStart;

    /**
     * Время окончания звонка (Unix time seconds).
     */
    @Column(name = "call_time_end")
    private long callTimeEnd;

    /**
     * Конструктор с параметрами для создания объекта Transactions.
     *
     * @param msisdns         объект Msisdns, связанный с транзакцией
     * @param msisdnId        первичный ключ Msisdn, связанного с данной транзакцией
     * @param callType        тип вызова
     * @param contactedMsisdn номер телефона абонента, с которым осуществлялась связь
     * @param callTimeStart   время начала звонка (Unix time seconds)
     * @param callTimeEnd     время окончания звонка (Unix time seconds)
     */
    public Transactions(Msisdns msisdns, String callType, String contactedMsisdn, long callTimeStart, long callTimeEnd) {
        this.msisdns = msisdns;
        this.msisdnId = msisdns != null ? msisdns.getId() : 1;
        this.callType = callType;
        this.contactedMsisdn = contactedMsisdn;
        this.callTimeStart = callTimeStart;
        this.callTimeEnd = callTimeEnd;
    }
}
