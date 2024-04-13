package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {
}
