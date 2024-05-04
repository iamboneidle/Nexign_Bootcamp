package brt.brt_service.Postgres.DAO.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Класс, представляющий собой сущность Users.
 */
@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Users {
    /**
     * Первичный ключ.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "name")
    private String name;

    /**
     * Фамилия пользователя.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Отчество пользователя.
     */
    @Column(name = "patronymic")
    private String patronymic;

    /**
     * Список номеров телефона пользователя.
     */
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    private List<Msisdns> msisdns;

    /**
     * Конструктор класса.
     *
     * @param name Имя.
     * @param surname Фамилия.
     * @param patronymic Отчество.
     */
    public Users(String name, String surname, String patronymic) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }
}
