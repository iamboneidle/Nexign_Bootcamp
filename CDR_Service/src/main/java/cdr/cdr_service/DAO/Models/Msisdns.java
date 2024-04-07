package cdr.cdr_service.DAO.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Msisdns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "msisdnId", fetch = FetchType.EAGER)
    private List<Transactions> transactions;

    public Msisdns(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}