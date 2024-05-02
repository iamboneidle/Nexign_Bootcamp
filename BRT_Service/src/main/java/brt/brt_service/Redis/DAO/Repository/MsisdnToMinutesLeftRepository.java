package brt.brt_service.Redis.DAO.Repository;

import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий абонентов и остатка их минут в Redis.
 */
@Repository
public interface MsisdnToMinutesLeftRepository extends CrudRepository<MsisdnToMinutesLeft, String> {
}
