package crm.crm_service.CRMSecurity;

import crm.crm_service.DAO.Models.Users;
import crm.crm_service.DAO.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис, который ищет пользователей из базы данных.
 */
@Service
public class CRMDetailsService implements UserDetailsService {
    /**
     * Репозиторий с данными о пользователях.
     */
    @Autowired
    private UsersRepository userRepository;

    /**
     * Метод нужен для поиска пользователя в базе данных сервиса.
     *
     * @param username the username identifying the user whose data is required.
     * @return Возвращает найденного пользователя.
     * @throws UsernameNotFoundException Исключение выбрасывается в том случае, если пользователь не был
     * найден по его имени в сервисе.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return user;
    }
}
