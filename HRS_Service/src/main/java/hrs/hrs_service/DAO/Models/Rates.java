package hrs.hrs_service.DAO.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Сущность Rates в Redis.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Rates")
public class Rates {
    /**
     * ID тарифа.
     */
    @Id
    private String id;
    /**
     * Название тарифа.
     */
    private String rateName;
    /**
     * Стартовая цена за месяц пользования.
     */
    private Float startCost;
    /**
     * Лимит минут.
     */
    private Long minLimit;
    /**
     * Стоимость исходящих звонков обслуживаемому абоненту.
     */
    private Float outcomingCallsCostServiced;
    /**
     * Стоимость исходящих звонков не обслуживаемому абоненту.
     */
    private Float outcomingCallsCostOthers;
    /**
     * Стоимость входящий звонков обслуживаемому абоненту.
     */
    private Float incomingCallsCostServiced;
    /**
     * Стоимость входящий звонков не обслуживаемому абоненту.
     */
    private Float incomingCallsCostOthers;

    /**
     * Перегруженный метод toString().
     *
     * @return Строка объекта.
     */
    @Override
    public String toString() {
        return "Rates{" + "id=" + id +
                ", rateName='" + rateName + '\'' +
                ", startCost=" + startCost +
                ", minLimit=" + minLimit +
                ", outcomingCallsCostServiced=" + outcomingCallsCostServiced +
                ", outcomingCallsCostOthers=" + outcomingCallsCostOthers +
                ", incomingCallsCostServiced=" + incomingCallsCostServiced +
                ", incomingCallsCostOthers=" + incomingCallsCostOthers +
                '}';
    }
}
