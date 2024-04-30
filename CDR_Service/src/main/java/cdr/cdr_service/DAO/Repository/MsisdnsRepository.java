package cdr.cdr_service.DAO.Repository;

import cdr.cdr_service.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для взаимодействия с сущностью Msisdn в базе данных.
 * <br>Используется для выполнения операций чтения, записи, обновления и удаления записей Msisdns в базе данных.
 */
@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {
}
