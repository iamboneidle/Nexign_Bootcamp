package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Calls;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CallsRepository extends JpaRepository<Calls, Long> {}