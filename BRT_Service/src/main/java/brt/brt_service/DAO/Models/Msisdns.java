package brt.brt_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Getter@Setter
public class Msisdns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rate")
    private Rates rates;

    @Column(name = "id_rate", insertable = false, updatable = false)
    private long rateId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_abonent")
    private Abonents abonents;

    @Column(name = "id_abonent", insertable = false, updatable = false)
    private long abonentId;

    @Column(name = "balance")
    private float balance;

    @Column(name = "incoming_calls_quantity")
    private long incomingCallsQuantity;

    @Column(name = "outcoming_calls_quantity")
    private long outcomingCallsQuantity;

    @Column(name = "minutes_left")
    private long minutesLeft;

    @OneToMany(mappedBy = "fromNumber", fetch = FetchType.EAGER)
    private List<Calls> outcomingCalls;

    @OneToMany(mappedBy = "toNumber", fetch = FetchType.EAGER)
    private List<Calls> incomingCalls;

    public Msisdns(
            String number,
            Rates rates,
            Abonents abonents,
            float balance,
            long incomingCallsQuantity,
            long outcomingCallsQuantity,
            long minutesLeft
    ) {
        this.number = number;
        this.rates = rates;
        this.abonents = abonents;
        this.balance = balance;
        this.incomingCallsQuantity = incomingCallsQuantity;
        this.outcomingCallsQuantity = outcomingCallsQuantity;
        this.minutesLeft = minutesLeft;
    }
}
