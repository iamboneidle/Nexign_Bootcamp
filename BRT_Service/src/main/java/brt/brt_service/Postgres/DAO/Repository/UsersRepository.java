package brt.brt_service.Postgres.DAO.Repository;

import brt.brt_service.Postgres.DAO.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий Users.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
