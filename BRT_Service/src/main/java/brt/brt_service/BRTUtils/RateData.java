package brt.brt_service.BRTUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные о тарифе.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RateData {
    /**
     * ID тарифа.
     */
    private Long id;
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
}
