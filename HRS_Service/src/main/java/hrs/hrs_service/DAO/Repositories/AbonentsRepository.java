package hrs.hrs_service.DAO.Repositories;


import hrs.hrs_service.DAO.Models.Abonents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonentsRepository extends JpaRepository<Abonents, Long> {
}
