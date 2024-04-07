package cdr.cdr_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "msisdn_id")
    private Msisdns msisdns;

    @Column(name = "msisdn_id", insertable = false, updatable = false)
    private Long msisdnId;

    @Column(name = "call_type")
    private String callType;

    @Column(name = "contacted_msisdn")
    private String contactedMsisdn;

    @Column(name = "call_time_start")
    private long callTimeStart;

    @Column(name = "call_time_end")
    private long callTimeEnd;

    public Transactions(Msisdns msisdns, Long msisdnId, String callType, String contactedMsisdn, long callTimeStart, long callTimeEnd) {
        this.msisdns = msisdns;
        this.msisdnId = msisdnId;
        this.callType = callType;
        this.contactedMsisdn = contactedMsisdn;
        this.callTimeStart = callTimeStart;
        this.callTimeEnd = callTimeEnd;
    }
}
