package brt.brt_service.Postgres.DAO.Repository;

import brt.brt_service.Postgres.DAO.Models.Msisdns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Репозиторий Msisdns.
 */
@Repository
public interface MsisdnsRepository extends JpaRepository<Msisdns, Long> {
    /**
     * Метод по поиску абонента по номеру телефона.
     *
     * @param number Номер телефона.
     * @return Абонент.
     */
    Msisdns findMsisdnsByNumber(String number);

    /**
     * Метод, списывающий деньги с баланса со счета абонента.
     *
     * @param number Номер телефона абонента.
     * @param amount Количество денег, которое нужно списать.
     */
    @Modifying
    @Query("UPDATE Msisdns m SET m.balance = m.balance - ?2 WHERE m.number = ?1")
    void writeOffMoneyByNumber(String number, float amount);

    /**
     * Метод, начисляющий деньги на баланс на счете абонента.
     *
     * @param number Номер телефона.
     * @param amount Количество денег, которое нужно начислить.
     */
    @Modifying
    @Query("UPDATE Msisdns m SET m.balance = m.balance + ?2 WHERE m.number = ?1")
    void putMoneyByNumber(String number, float amount);

    /**
     * Метод, который увеличивает число исходящих вызовов абонента на 1.
     *
     * @param number Номер телефона абонента.
     */
    @Modifying
    @Query("UPDATE Msisdns m SET m.outcomingCallsQuantity = m.outcomingCallsQuantity + 1 WHERE m.number = ?1")
    void increaseOutcomingCallsQuantity(String number);

    /**
     * Метод, который увеличивает число входящих вызовов абонента на 1.
     *
     * @param number Номер телефона абонента.
     */
    @Modifying
    @Query("UPDATE Msisdns m SET m.incomingCallsQuantity = m.incomingCallsQuantity + 1 WHERE m.number = ?1")
    void increaseIncomingCallsQuantity(String number);

}
