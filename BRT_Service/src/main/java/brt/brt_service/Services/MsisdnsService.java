package brt.brt_service.Services;

import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsisdnsService {
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    public List<Msisdns> getMsisdns() {
        return msisdnsRepository.findAll();
    }

    public long getRateIdByPhoneNumber(String phoneNumber) {
        Msisdns msisdns = msisdnsRepository.findMsisdnsByNumber(phoneNumber);
        return msisdns.getRateId();
    }
}
