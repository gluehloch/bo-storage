package de.winkler.betoffice.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration for dev environment.
 */
@Profile(value = "dev")
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = { "classpath:/bodev.properties" })
public class DevelopmentPropertiesConfiguration extends AbstractPropertiesConfiguration {

    @Bean
    BetofficeProperties betofficeProperties() {
        return getProperties();
    }

}
