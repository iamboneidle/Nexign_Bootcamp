package brt.brt_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Abonents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @OneToMany(mappedBy = "abonentId", fetch = FetchType.EAGER)
    private List<Msisdns> msisdns;

    public Abonents(String name, String surname, String patronymic, List<Msisdns> msisdns) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }
}

