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
@Getter
@Setter
public class Msisdns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rate")
    private Rates rates;

    @Column(name = "id_rate", insertable = false, updatable = false)
    private Long rateId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private Users users;

    @Column(name = "id_user", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "balance")
    private Float balance;

    @Column(name = "incoming_calls_quantity")
    private Long incomingCallsQuantity;

    @Column(name = "outcoming_calls_quantity")
    private Long outcomingCallsQuantity;

    @OneToMany(mappedBy = "fromNumber", fetch = FetchType.EAGER)
    private List<Calls> outcomingCalls;

    @OneToMany(mappedBy = "toNumber", fetch = FetchType.EAGER)
    private List<Calls> incomingCalls;

    public Msisdns(
            String number,
            Rates rates,
            Users users,
            float balance,
            long incomingCallsQuantity,
            long outcomingCallsQuantity
    ) {
        this.number = number;
        this.rates = rates;
        this.users = users;
        this.balance = balance;
        this.incomingCallsQuantity = incomingCallsQuantity;
        this.outcomingCallsQuantity = outcomingCallsQuantity;
    }

    @Override
    public String toString() {
        return "Msisdns{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", incomingCallsQuantity=" + incomingCallsQuantity +
                ", outcomingCallsQuantity=" + outcomingCallsQuantity +
                '}';
    }
}
