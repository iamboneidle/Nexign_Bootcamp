package cdr.cdr_service.DAO.Repository;

import cdr.cdr_service.DAO.Models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {}
