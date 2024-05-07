package brt.brt_service.BRTUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

/**
 * Данные из Кафки.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class DataFromKafka {
    /**
     * Имя файла.
     */
    private String fileName;
    /**
     * Содержание файла.
     */
    private String fileContent;
}
