package de.winkler.betoffice.conf;

import java.util.Properties;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Registration Service Configuration Factory.
 * <br/>
 * Remember: The last property source wins on property name clash!
 * <br/>
 * {@code /register.properties} must be located under {@link src/test/resources}.
 *
 * @author Andre Winkler
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"de.winkler.betoffice", "de.betoffice"})
@EnableJpaRepositories(basePackages = { "de.winkler.betoffice" })
public class PersistenceJPAConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        p.setIgnoreResourceNotFound(true);
        return p;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setJpaVendorAdapter(vendorAdapter);
      factory.setPackagesToScan("de.winkler.betoffice");
      factory.setDataSource(dataSource);
      factory.setJpaProperties(additionalProperties());
      factory.afterPropertiesSet();

      return factory.getObject();
    }

    /*
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "de.awtools.registration" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }
    */

    @Bean
    public DataSource dataSource(BetofficeProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        Properties connectionProperties = new java.util.Properties();
        connectionProperties.setProperty("passwordCharacterEncoding", "UTF-8");
        connectionProperties.setProperty("autocommit", "false");
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        return properties;
    }

}
