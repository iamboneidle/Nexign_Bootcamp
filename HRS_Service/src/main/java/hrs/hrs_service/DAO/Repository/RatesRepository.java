package hrs.hrs_service.DAO.Repository;

import hrs.hrs_service.DAO.Models.Rates;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности Rates в Redis.
 */
@Repository
public interface RatesRepository extends CrudRepository<Rates, String> {
}
