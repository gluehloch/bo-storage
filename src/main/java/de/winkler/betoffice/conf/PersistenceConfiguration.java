package de.winkler.betoffice.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Persistence configuration.
 *
 * @author winkler
 */
@Component
public class PersistenceConfiguration {

    @Value("${betoffice.persistence.classname}")
    private String driverClassName;

    @Value("${betoffice.persistence.url}")
    private String url;
    
    @Value("${betoffice.persistence.username}")
    private String username;
    
    @Value("${betoffice.persistence.password}")
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
