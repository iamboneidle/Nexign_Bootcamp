package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BRTService {
    @Autowired
    private CDRFileHandlerService callRecordsHandlerService;
    @Autowired
    private CallReceiptHandlerService callReceiptHandlerService;

    public void handleCDRFile(String callDataRecord) {
        callRecordsHandlerService.makeCallRecords(callDataRecord);
    }

    public void handleCallReceipt(CallReceipt callReceipt) { callReceiptHandlerService.validateCallReceipt(callReceipt); }

    private void sendRatesToCacheDB_HRS() {/* TODO: @PostConstruct */ }
}
