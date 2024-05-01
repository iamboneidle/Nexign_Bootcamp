package brt.brt_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий собой сущность Calls.
 */
@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Calls {
    /**
     * Первичный ключ.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Абонент, который звонил.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_from_number")
    private Msisdns fromMsisdns;

    /**
     * ID абонента, который звонил.
     */
    @Column(name = "id_from_number", insertable = false, updatable = false)
    private Long fromNumber;

    /**
     * Абонент, которому звонили.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_to_number")
    private Msisdns toMsisdns;

    /**
     * ID абонента, которому звонили.
     */
    @Column(name = "id_to_number", insertable = false, updatable = false)
    private Long toNumber;

    /**
     * Время начала звонка в Unix time seconds.
     */
    @Column(name = "call_time_start")
    private Long callTimeStart;

    /**
     * Время окончания звонка в Unix time seconds.
     */
    @Column(name = "call_time_end")
    private Long callTimeEnd;

    /**
     * Длительность звонка в Unix time seconds.
     */
    @Column(name = "call_duration")
    private Long callDuration;

    /**
     * CDR файл, в котором поступил звонок.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cdr")
    private CallDataRecords cdr;

    /**
     * ID CDR файла, в котром поступил звонок.
     */
    @Column(name = "id_cdr", insertable = false, updatable = false)
    private Long cdrId;

    /**
     * Конструктор класса.
     *
     * @param fromMsisdns Абонент, который звонил.
     * @param toMsisdns Абонент, которому звонили.
     * @param callTimeStart Время начала звонка в Unix time seconds.
     * @param callTimeEnd Время окончания звонка в Unix time seconds.
     * @param callDuration Длительность звонка в Unix time seconds.
     * @param cdr CDR файл, в котором пришел звонок.
     */
    public Calls(Msisdns fromMsisdns,
                 Msisdns toMsisdns,
                 Long callTimeStart,
                 Long callTimeEnd,
                 Long callDuration,
                 CallDataRecords cdr
    ) {
        this.fromMsisdns = fromMsisdns;
        this.toMsisdns = toMsisdns;
        this.callTimeStart = callTimeStart;
        this.callTimeEnd = callTimeEnd;
        this.callDuration = callDuration;
        this.cdr = cdr;
    }
}
