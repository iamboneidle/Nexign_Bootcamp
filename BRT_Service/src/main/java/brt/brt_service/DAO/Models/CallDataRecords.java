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
public class CallDataRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "cdrId", fetch = FetchType.EAGER)
    private List<Calls> calls;

    @Column(name = "time_formed")
    private long timeFormed;

    public CallDataRecords(List<Calls> calls, long timeFormed) {
        this.calls = calls;
        this.timeFormed = timeFormed;
    }
}
