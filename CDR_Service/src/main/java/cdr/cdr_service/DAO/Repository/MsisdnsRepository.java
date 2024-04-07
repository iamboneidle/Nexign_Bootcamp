package cdr.cdr_service.DAO.Repository;

import cdr.cdr_service.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {

    Msisdns findByPhoneNumber(String phoneNumber);
}
