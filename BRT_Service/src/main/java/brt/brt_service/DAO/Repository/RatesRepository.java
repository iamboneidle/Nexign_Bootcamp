package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий Rates.
 */
@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {
    /**
     * Метод по поиску Тарифа по его ID.
     *
     * @param id ID тарифа.
     * @return Тариф.
     */
    Rates findRatesById(Long id);
}
