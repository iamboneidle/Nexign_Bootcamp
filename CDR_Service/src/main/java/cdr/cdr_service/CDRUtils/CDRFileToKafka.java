package cdr.cdr_service.CDRUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для Json'а с названием и содержанием файла.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class CDRFileToKafka {
    /**
     * Название файла.
     */
    private String fileName;
    /**
     * Содержание файла.
     */
    private String fileContent;
}
