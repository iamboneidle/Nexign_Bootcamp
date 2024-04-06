package cdr.cdr_service.Services;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsisdnsService {
    private MsisdnsRepository msisdnsRepository;

    public MsisdnsService(MsisdnsRepository msisdnsRepository) {
        this.msisdnsRepository = msisdnsRepository;
    }

    public List<Msisdns> getMsisdns() {
        msisdnsRepository.findAll().forEach(System.out::println);
        return msisdnsRepository.findAll();
    }
}
