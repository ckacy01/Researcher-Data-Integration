package org.technoready.researcherdataintegration.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuration class responsible for database connection and JPA/Hibernate setup.
 * Configures HikariCP connection pool, entity manager factory, and transaction management.
 * DATE: 08 - October - 2025
 *
 * This configuration uses HikariCP for optimal connection pooling performance and
 * Spring Data JPA with Hibernate as the JPA provider.
 *
 * Connection pool settings:
 * - Maximum pool size: 10 connections
 * - Minimum idle: 5 connections
 * - Connection timeout: 30 seconds
 * - Idle timeout: 10 minutes
 * - Max lifetime: 30 minutes
 *
 * @author Jorge Armando Avila Carrillo | NAOID: 3310
 * @version 1.0
 */
@Data
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;

    /**
     * Creates and configures the HikariCP DataSource bean for database connections.
     * Configures connection pool settings optimized for application performance.
     *
     * @return DataSource - Configured HikariCP data source with connection pooling
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName(driverClassName);

        // Connection pool optimization settings
        config.setMaximumPoolSize(10);           // Maximum number of connections in pool
        config.setMinimumIdle(5);                // Minimum idle connections to maintain
        config.setConnectionTimeout(30000);      // 30 seconds timeout for connection acquisition
        config.setIdleTimeout(600000);           // 10 minutes before idle connection is removed
        config.setMaxLifetime(1800000);          // 30 minutes maximum connection lifetime

        return new HikariDataSource(config);
    }

    /**
     * Creates and configures the JPA EntityManagerFactory bean.
     * Sets up Hibernate as the JPA provider and configures entity scanning and JPA properties.
     *
     * @return LocalContainerEntityManagerFactoryBean - Configured entity manager factory
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.technoready.researcherdataintegration.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Hibernate configuration properties
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.jdbc.lob.non_contextual_creation", "true");
        em.setJpaProperties(properties);

        return em;
    }

    /**
     * Creates and configures the JPA transaction manager bean.
     * Enables declarative transaction management for the application.
     *
     * @return PlatformTransactionManager - Configured JPA transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}