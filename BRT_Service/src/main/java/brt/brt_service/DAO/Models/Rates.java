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
    private Long id;

    @Column(name = "rate_name")
    private String rateName;

    @Column(name = "start_cost")
    private Float startCost;

    @Column(name = "min_limit")
    private Float minLimit;

    @Column(name = "out_calls_cost_serviced")
    private Float outcomingCallsCostServiced;

    @Column(name = "out_calls_cost_others")
    private Float outcomingCallsCostOthers;

    @Column(name = "in_calls_cost_serviced")
    private Float incomingCallsCostServiced;

    @Column(name = "in_calls_cost_others")
    private Float incomingCallsCostOthers;

    @OneToMany(mappedBy = "rateId", fetch = FetchType.EAGER)
    private List<Msisdns> msisdns;

    public Rates(
            String rateName,
            Float startCost,
            Float minLimit,
            Float outcomingCallsCostServiced,
            Float outcomingCallsCostOthers,
            Float incomingCallsCostServiced,
            Float incomingCallsCostOthers,
            List<Msisdns> msisdns
    ) {
        this.rateName = rateName;
        this.startCost = startCost;
        this.minLimit = minLimit;
        this.outcomingCallsCostServiced = outcomingCallsCostServiced;
        this.outcomingCallsCostOthers = outcomingCallsCostOthers;
        this.incomingCallsCostServiced = incomingCallsCostServiced;
        this.incomingCallsCostOthers = incomingCallsCostOthers;
        this.msisdns = msisdns;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "id=" + id +
                ", rateName='" + rateName + '\'' +
                ", startCost=" + startCost +
                ", minLimit=" + minLimit +
                ", outcomingCallsCostServiced=" + outcomingCallsCostServiced +
                ", outcomingCallsCostOthers=" + outcomingCallsCostOthers +
                ", incomingCallsCostServiced=" + incomingCallsCostServiced +
                ", incomingCallsCostOthers=" + incomingCallsCostOthers +
                '}';
    }
}
