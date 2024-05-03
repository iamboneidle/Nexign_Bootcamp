package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Services.Handlers.CDRFileHandlerService;
import brt.brt_service.Services.Handlers.CallReceiptHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BRTService {
    /**
     * Сервис, обрабатывающий информацию о звонках.
     */
    @Autowired
    private CDRFileHandlerService callRecordsHandlerService;
    /**
     * Сервис, обрабатывающий информацию о чеках по звонкам.
     */
    @Autowired
    private CallReceiptHandlerService callReceiptHandlerService;

    /**
     * Метод, который передает с контроллера CDR файл на обработку в CallRecordsHandlerService.
     *
     * @param callDataRecord Содержание CDR файла.
     * @param fileName       Имя CDR файла.
     */
    public void handleCDRFile(String callDataRecord, String fileName) {
        callRecordsHandlerService.makeCallRecords(callDataRecord, fileName);
    }

    /**
     * Метод, который передает с контроллера чек по звонку в CallReceiptHandlerService.
     *
     * @param callReceipt Объект с данными чека по звонку.
     */
    public void handleCallReceipt(CallReceipt callReceipt) {
        callReceiptHandlerService.validateCallReceipt(callReceipt);
    }
}
