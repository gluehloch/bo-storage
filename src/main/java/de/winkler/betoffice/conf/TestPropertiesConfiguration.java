package de.winkler.betoffice.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration for dev environment.
 */
@Profile(value = "test")
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = { "classpath:/botest.properties" })
public class TestPropertiesConfiguration extends AbstractPropertiesConfiguration {

    @Bean
    BetofficeProperties betofficeProperties() {
        return getProperties();
    }

}
