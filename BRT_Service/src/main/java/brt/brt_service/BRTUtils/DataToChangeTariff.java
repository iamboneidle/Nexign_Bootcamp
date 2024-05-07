package brt.brt_service.BRTUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для изменения тарифа.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

