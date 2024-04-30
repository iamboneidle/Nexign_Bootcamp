package brt.brt_service.DAO.Repository;

import brt.brt_service.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {
    Msisdns findMsisdnsByNumber(String number);

    @Modifying
    @Query("UPDATE Msisdns m SET m.balance = m.balance - ?2 WHERE m.number = ?1")
    void writeOffMoneyByNumber(String number, float amount);

    @Modifying
    @Query("UPDATE Msisdns m SET m.balance = m.balance + ?2 WHERE m.number = ?1")
    void putMoneyByNumber(String number, float amount);

    @Modifying
    @Query("UPDATE Msisdns m SET m.outcomingCallsQuantity = m.outcomingCallsQuantity + 1 WHERE m.number = ?1")
    void increaseOutcomingCallsQuantity(String number);

    @Modifying
    @Query("UPDATE Msisdns m SET m.incomingCallsQuantity = m.incomingCallsQuantity + 1 WHERE m.number = ?1")
    void increaseIncomingCallsQuantity(String number);

}
