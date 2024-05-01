package cdr.cdr_service.DAO.Repository;

import cdr.cdr_service.DAO.Models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для взаимодействия с сущностью Transactions в базе данных.
 * <br>Используется для выполнения операций чтения, записи, обновления и удаления записей Transactions в базе данных.
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}
