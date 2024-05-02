package brt.brt_service.Postgres.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Класс, представляющей собой сущность CallDataRecords.
 */
@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class CallDataRecords {
    /**
     * Первичный ключ.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Список звонков из данного CDR файла.
     */
    @OneToMany(mappedBy = "cdrId", fetch = FetchType.EAGER)
    private List<Calls> calls;

    /**
     * Время, когда CDR файл поступил на BRT.
     */
    @Column(name = "time_formed")
    private Long timeFormed;

    /**
     * Конструктор класса.
     *
     * @param timeFormed Время, когда CDR файл поступил на BRT.
     */
    public CallDataRecords(long timeFormed) {
        this.timeFormed = timeFormed;
    }
}
