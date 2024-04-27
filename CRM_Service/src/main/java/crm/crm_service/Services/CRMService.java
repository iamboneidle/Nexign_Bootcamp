package crm.crm_service.Services;

import crm.crm_service.DAO.Repository.RolesRepository;
import crm.crm_service.DAO.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRMService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UsersRepository usersRepository;

    public void pay() {

    }

    public void save() {

    }

    public void changeTariff() {

    }

    public void addSubscriber() {

    }
}
