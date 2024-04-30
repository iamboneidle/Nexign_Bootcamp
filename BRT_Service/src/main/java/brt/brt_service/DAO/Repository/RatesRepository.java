package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {
    Rates findRatesById(Long id);
}
