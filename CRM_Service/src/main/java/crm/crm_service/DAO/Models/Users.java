package crm.crm_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_roles")
    private Roles role;
    @Column(name = "id_roles", insertable = false, updatable = false)
    private Long roleId;
}
