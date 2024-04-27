package brt.brt_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Calls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_from_number")
    private Msisdns fromMsisdns;

    @Column(name = "id_from_number", insertable = false, updatable = false)
    private Long fromNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_to_number")
    private Msisdns toMsisdns;

    @Column(name = "id_to_number", insertable = false, updatable = false)
    private Long toNumber;

    @Column(name = "call_time_start")
    private Long callTimeStart;

    @Column(name = "call_time_end")
    private Long callTimeEnd;

    @Column(name = "call_duration")
    private Long callDuration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cdr")
    private CallDataRecords cdr;

    @Column(name = "id_cdr", insertable = false, updatable = false)
    private Long cdrId;

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
