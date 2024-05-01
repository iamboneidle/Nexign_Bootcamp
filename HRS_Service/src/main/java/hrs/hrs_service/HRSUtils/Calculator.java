package hrs.hrs_service.HRSUtils;

import hrs.hrs_service.Services.HRSService;

/**
 * Класс, представляющий собой поток, который производит расчет по звонку.
 */
public class Calculator implements Runnable {
    /**
     * HRSService, производящий расчет.
     */
    private final HRSService hrsService;
    /**
     * Объект с данными о звонке.
     */
    private final DataToPay dataToPay;

    /**
     * Конструктор класса.
     *
     * @param hrsService Сервис, производящий расчет.
     * @param dataToPay Объект с данными о звонке.
     */
    public Calculator(HRSService hrsService, DataToPay dataToPay) {
        this.hrsService = hrsService;
        this.dataToPay = dataToPay;
    }

    /**
     * Задача потоку на расчет.
     */
    @Override
    public void run() {
        hrsService.makeAndSendCallReceipt(dataToPay);
    }
}
