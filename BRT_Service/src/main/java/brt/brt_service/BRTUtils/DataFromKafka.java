package brt.brt_service.BRTUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
