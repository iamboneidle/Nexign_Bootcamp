package hrs.hrs_service.DAO.Repositories;

import hrs.hrs_service.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {
    Msisdns findMsisdnsByNumber(String number);
}
