package cdr.cdr_service.Services;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с объектами Msisdns.
 */
@Service
public class MsisdnsService {
    /**
     * Репозиторий Msisdns.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    /**
     * Метод для получения списка всех объектов Msisdns из базы данных.
     *
     * @return список объектов Msisdns.
     */
    public List<Msisdns> getMsisdns() {
        return msisdnsRepository.findAll();
    }
}

