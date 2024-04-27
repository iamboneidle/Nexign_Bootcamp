package crm.crm_service.DAO.Repository;

import crm.crm_service.DAO.Models.Users;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
}
