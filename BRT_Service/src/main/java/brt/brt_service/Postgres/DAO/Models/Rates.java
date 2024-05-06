package brt.brt_service.Postgres.DAO.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Класс, представляющий собой сущность Rates.
 */
@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rates {
    /**
     * Первичный ключ.
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * Название тарифа.
     */
    @Column(name = "rate_name")
    private String rateName;

    /**
     * Стартовая цена (для помесячного тарифа).
     */
    @Column(name = "start_cost")
    private Float startCost;

    /**
     * Лимит минут (для помесячного тарифа).
     */
    @Column(name = "min_limit")
    private Long minLimit;

    /**
     * Цена для исходящих звонков обслуживаемому абоненту (для классического тарифа).
     */
    @Column(name = "out_calls_cost_serviced")
    private Float outcomingCallsCostServiced;

    /**
     * Цена исходящих звонков необслуживаемому абоненту (для классического тарифа).
     */
    @Column(name = "out_calls_cost_others")
    private Float outcomingCallsCostOthers;

    /**
     * Цена входящих звонков обслуживаемому абоненту (для классического тарифа).
     */
    @Column(name = "in_calls_cost_serviced")
    private Float incomingCallsCostServiced;

    /**
     * Цена входящих звонков необслуживаемому абоненту (для классического тарифа).
     */
    @Column(name = "in_calls_cost_others")
    private Float incomingCallsCostOthers;

    /**
     * Список абонентов.
     */
    @OneToMany(mappedBy = "rateId", fetch = FetchType.EAGER)
    private List<Msisdns> msisdns;

    /**
     * Конструктор класса.
     *
     * @param id Первичный ключ.
     * @param rateName Название тарифа.
     * @param startCost Стартовая цена (для помесячного тарифа).
     * @param minLimit Лимит минут (для помесячного тарифа).
     * @param outcomingCallsCostServiced Цена для исходящих звонков обслуживаемому абоненту (для классического тарифа).
     * @param outcomingCallsCostOthers Цена исходящих звонков необслуживаемому абоненту (для классического тарифа).
     * @param incomingCallsCostServiced Цена входящих звонков обслуживаемому абоненту (для классического тарифа).
     * @param incomingCallsCostOthers Цена входящих звонков необслуживаемому абоненту (для классического тарифа).
     */
    public Rates(
            Long id,
            String rateName,
            Float startCost,
            Long minLimit,
            Float outcomingCallsCostServiced,
            Float outcomingCallsCostOthers,
            Float incomingCallsCostServiced,
            Float incomingCallsCostOthers
    ) {
        this.id = id;
        this.rateName = rateName;
        this.startCost = startCost;
        this.minLimit = minLimit;
        this.outcomingCallsCostServiced = outcomingCallsCostServiced;
        this.outcomingCallsCostOthers = outcomingCallsCostOthers;
        this.incomingCallsCostServiced = incomingCallsCostServiced;
        this.incomingCallsCostOthers = incomingCallsCostOthers;
    }
}
