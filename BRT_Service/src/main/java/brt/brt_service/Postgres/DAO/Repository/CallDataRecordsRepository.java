package brt.brt_service.Postgres.DAO.Repository;

import brt.brt_service.Postgres.DAO.Models.CallDataRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий CallDataRecords.
 */
@Repository
public interface CallDataRecordsRepository extends JpaRepository<CallDataRecords, Long> {
}
