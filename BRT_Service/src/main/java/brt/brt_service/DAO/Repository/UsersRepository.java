package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий Users.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
