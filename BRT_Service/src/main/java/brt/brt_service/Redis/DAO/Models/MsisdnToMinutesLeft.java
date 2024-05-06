package brt.brt_service.Redis.DAO.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Класс представляющий собой сущность в Redis, ключ-номер абонента, значение-остаток минут.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
}
