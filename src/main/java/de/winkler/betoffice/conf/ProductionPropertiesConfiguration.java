package de.winkler.betoffice.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

//@Profile(value = "production")
@Configuration
@PropertySource(ignoreResourceNotFound = false, value = {
        /*"file:${user.home}/.betoffice.properties",*/
        "file:${AWTOOLS_CONFDIR}/betoffice/betoffice.properties"
})
public class ProductionPropertiesConfiguration extends AbstractPropertiesConfiguration {

    @Bean
    BetofficeProperties betofficeProperties() {
        return getProperties();
    }

}
