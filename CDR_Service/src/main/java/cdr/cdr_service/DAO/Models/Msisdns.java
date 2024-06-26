package cdr.cdr_service.DAO.Models;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Сущность в базе данных под названием Msisdns.
 * <br>Представляет из себя номер телефона абонента и SERIAL PRIMARY KEY id абонента.
 * <br> Нужна для хранения обслуживаемых абонентов в базе данных перед стартом приложения ля генерации их звонков.
 */
@Entity
@Data
@NoArgsConstructor
@ToString
public class Msisdns {
    /**
     * Первичный ключ для сущности Msisdns.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Номер телефона абонента.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Список транзакций абонента, использующего определенный номер телефона.
     */
    @OneToMany(mappedBy = "msisdnId", fetch = FetchType.LAZY)
    private List<Transactions> transactions;

    /**
     * Конструктор с номером телефона в качестве параметра.
     *
     * @param phoneNumber Номер телефона
     */
    public Msisdns(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
