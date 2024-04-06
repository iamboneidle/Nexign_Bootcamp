package cdr.cdr_service.DAO.Models;

import lombok.Data;
import javax.persistence.*;


@Entity
@Data
public class Msisdns {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="phoneNumber")
    private String phoneNumber;

    public Msisdns(Long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public Msisdns() {

    }

}