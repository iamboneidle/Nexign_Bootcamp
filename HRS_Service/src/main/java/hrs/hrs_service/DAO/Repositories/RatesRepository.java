package hrs.hrs_service.DAO.Repositories;

import hrs.hrs_service.DAO.Models.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {
}
