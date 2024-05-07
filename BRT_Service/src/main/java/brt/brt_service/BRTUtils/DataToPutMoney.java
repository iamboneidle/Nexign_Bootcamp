package brt.brt_service.BRTUtils;

import lombok.*;

/**
 * Класс, представляющий собой данные для пополнения баланса счета абонента.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataToPutMoney {
    /**
     * Номер телефона абонента.
     */
    private String msisdn;
    /**
     * Количество денег для пополнения баланса.
     */
    private Float money;
}

