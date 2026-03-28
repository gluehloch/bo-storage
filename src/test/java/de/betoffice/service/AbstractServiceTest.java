package de.betoffice.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import de.betoffice.conf.PersistenceJPAConfiguration;
import de.betoffice.conf.TestPropertiesConfiguration;

/**
 * Abstract helper class to init the spring test configuration.
 * 
 * @author Andre Winkler
 */
@SpringJUnitConfig(classes = { PersistenceJPAConfiguration.class, TestPropertiesConfiguration.class })
@ActiveProfiles(profiles = "test")
@ComponentScan({ "de.betoffice", "de.betoffice" })
public abstract class AbstractServiceTest {

}
