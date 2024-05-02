package brt.brt_service.Postgres.DAO.Repository;

import brt.brt_service.Postgres.DAO.Models.Calls;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий Calls.
 */
@Repository
public interface CallsRepository extends JpaRepository<Calls, Long> {
}
