package de.betoffice.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.betoffice.conf.PersistenceJPAConfiguration;
import de.betoffice.conf.TestPropertiesConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Abstract helper class to init the spring test configuration.
 * 
 * @author Andre Winkler
 */
@SpringJUnitConfig(classes = { PersistenceJPAConfiguration.class, TestPropertiesConfiguration.class })
@ActiveProfiles(profiles = "test")
// @ExtendWith(SpringExtension.class)
// @ContextConfiguration(classes = { PersistenceJPAConfiguration.class, TestPropertiesConfiguration.class })
@ComponentScan({"de.betoffice", "de.betoffice"})
public abstract class AbstractServiceTest {

}
