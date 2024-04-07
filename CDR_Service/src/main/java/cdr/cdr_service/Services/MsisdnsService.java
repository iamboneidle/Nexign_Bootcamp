package cdr.cdr_service.Services;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsisdnsService {
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    public MsisdnsService(MsisdnsRepository msisdnsRepository) {
        this.msisdnsRepository = msisdnsRepository;
    }

    public List<Msisdns> getMsisdns() {
        List<Msisdns> all = msisdnsRepository.findAll();
        all.forEach(msisdns -> System.out.println(msisdns.getId() + "   " + msisdns.getPhoneNumber()));
        return all;
    }

    public long getIdByMsisdns(String phoneNumber) {
        Msisdns byPhoneNumber = msisdnsRepository.findByPhoneNumber(phoneNumber);
        return byPhoneNumber.getId();
    }

}

