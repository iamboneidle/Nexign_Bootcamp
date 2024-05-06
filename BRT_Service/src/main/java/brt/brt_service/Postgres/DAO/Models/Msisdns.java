package brt.brt_service.Postgres.DAO.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Сущность Msisdns.
 */
@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Msisdns {
    /**
     * Первичный ключ.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Номер телефона абонента.
     */
    @Column(name = "number")
    private String number;

    /**
     * Тариф абонента.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rate")
    private Rates rates;

    /**
     * ID тарифа абонента.
     */
    @Column(name = "id_rate", insertable = false, updatable = false)
    private Long rateId;

    /**
     * Пользователь.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private Users users;

    /**
     * ID пользователя.
     */
    @Column(name = "id_user", insertable = false, updatable = false)
    private Long userId;

    /**
     * Баланс абонента.
     */
    @Column(name = "balance")
    private Float balance;

    /**
     * Количество входящих звонков.
     */
    @Column(name = "incoming_calls_quantity")
    private Long incomingCallsQuantity;

    /**
     * Количество исходящих звонков.
     */
    @Column(name = "outcoming_calls_quantity")
    private Long outcomingCallsQuantity;

    /**
     * Список исходщих звонков.
     */
    @OneToMany(mappedBy = "fromNumber", fetch = FetchType.EAGER)
    private List<Calls> outcomingCalls;

    /**
     * Список входящих звонков.
     */
    @OneToMany(mappedBy = "toNumber", fetch = FetchType.EAGER)
    private List<Calls> incomingCalls;

    /**
     * Конструктор класса.
     *
     * @param number Номер телефона абонента.
     * @param rates Тариф.
     * @param users Пользователь.
     * @param balance Баланс.
     * @param incomingCallsQuantity Количество входящих звонков.
     * @param outcomingCallsQuantity Количество исходящих звонков.
     */
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
}
