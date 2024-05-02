package brt.brt_service.Redis.DAO.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Класс представляющий собой сущность в Redis, ключ-номер абонента, значение-остаток минут.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("MsisdnToMinutesLeft")
public class MsisdnToMinutesLeft {
    /**
     * Ключ, номер телефона абонента.
     */
    @Id
    private String msisdn;
    /**
     * Остаток минут.
     */
    private Long minutesLeft;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "MsisdnToMinutesLeft{" + "msisdn='" + msisdn + '\'' +
                ", minutesLeft=" + minutesLeft +
                '}';
    }
}
