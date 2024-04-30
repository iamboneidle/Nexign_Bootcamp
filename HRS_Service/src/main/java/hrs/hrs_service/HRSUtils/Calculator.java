package hrs.hrs_service.HRSUtils;

import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.Services.HRSService;

public class Calculator implements Runnable {
    private final HRSService hrsService;
    private final DataToPay dataToPay;

    public Calculator(HRSService hrsService, DataToPay dataToPay) {
        this.hrsService = hrsService;
        this.dataToPay = dataToPay;
    }

    @Override
    public void run() {
        hrsService.makeAndSendCallReceipt(dataToPay);
    }
}
