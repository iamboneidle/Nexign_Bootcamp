package hrs.hrs_service.DAO.Repositories;

import hrs.hrs_service.DAO.Models.CallDataRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallDataRecordsRepository extends JpaRepository<CallDataRecords, Long> {
}
