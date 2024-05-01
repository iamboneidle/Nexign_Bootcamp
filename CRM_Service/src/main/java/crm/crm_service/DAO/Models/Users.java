package crm.crm_service.DAO.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Класса, представляющий собой сущность в базе данных в виде абонента.
 */
@Entity
@Data
@NoArgsConstructor
public class Users implements UserDetails {
    /**
     * Первичный ключ Users.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Имя пользователя в CRM (админ-"admin", пользователь-"msisdn" [msisdn-номер телефона абонента]).
     */
    @Column(name = "username")
    private String username;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password")
    private String password;

    /**
     * Роль прользователя.
     */
    @Getter
    @Column(name = "role")
    private String roles;

    /**
     * Конструктор класса.
     *
     * @param username Имя пользователя в системе.
     * @param password Пароль пользователя.
     * @param roles Роль пользователя.
     */
    public Users(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    /**
     * Метод получения списка ролей пользователя.
     *
     * @return Список ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles));
    }

    /**
     * Метод для получения пароля пользователя.
     *
     * @return Пароль пользователя.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Метод для получения имени пользователя в системе.
     *
     * @return Имя пользователя в системе.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Метод для получения информации о том, не просрочен ли аккаунт.
     *
     * @return true - не просрочен, false - просрочен.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Метод для получения информации о том, не заблокирован ли аккаунт.
     *
     * @return true - не заблокирован, false - заблокирован.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Метод для получения информации о том, не просрочены ли учетные данные.
     *
     * @return true - не просрочены, false - просрочены.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Метод для получения информации о том, доступен ли аккаунт.
     *
     * @return true - доступен, false - не доступен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
