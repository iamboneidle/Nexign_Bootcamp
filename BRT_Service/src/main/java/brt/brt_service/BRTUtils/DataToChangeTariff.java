package brt.brt_service.BRTUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для изменения тарифа.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataToChangeTariff {
    /**
     * Номе телефона абонента.
     */
    private String msisdn;
    /**
     * ID тарифа.
     */
    private Long tariffId;
}
