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
public class Rates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "rate_name")
    private String rateName;

    @Column(name = "start_cost")
    private float startCost;

    @Column(name = "min_limit")
    private float minLimit;

    @Column(name = "out_calls_cost_serviced")
    private float outcomingCallsCostServiced;

    @Column(name = "out_calls_cost_others")
    private float outcomingCallsCostOthers;

    @Column(name = "in_calls_cost_serviced")
    private float incomingCallsCostServiced;

    @Column(name = "in_calls_cost_others")
    private float incomingCallsCostOthers;

    @OneToMany(mappedBy = "rateId", fetch = FetchType.EAGER)
    private List<Msisdns> msisdns;

    public Rates(
            String rateName,
            long startCost,
            long minLimit,
            long outcomingCallsCostServiced,
            long outcomingCallsCostOthers,
            long incomingCallsCostServiced,
            long incomingCallsCostOthers
    ) {
        this.rateName = rateName;
        this.startCost = startCost;
        this.minLimit = minLimit;
        this.outcomingCallsCostServiced = outcomingCallsCostServiced;
        this.outcomingCallsCostOthers = outcomingCallsCostOthers;
        this.incomingCallsCostServiced = incomingCallsCostServiced;
        this.incomingCallsCostOthers = incomingCallsCostOthers;
    }
}
