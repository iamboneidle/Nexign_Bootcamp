package brt.brt_service.Configs;

import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Конфигурационный файл для базы данных Postgres.
 */
@Configuration
public class PostgresConfig {
    @Autowired
    private Environment env;

    /**
     * Создает и настраивает бин DataSource для подключения к базе данных PostgreSQL.
     * Получает свойства подключения к базе данных, такие как имя класса драйвера, URL, имя пользователя и пароль, из окружения.
     *
     * @return Бин DataSource, настроенный для подключения к базе данных PostgreSQL.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
}