package crm.crm_service.DAO.Repository;

import crm.crm_service.DAO.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для пользователей.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    /**
     * Метод, который возвращает из базы данных пользователя по его имени.
     *
     * @param username Имя пользователя.
     * @return Найденный пользователь.
     */
    Users findByUsername(String username);
}
