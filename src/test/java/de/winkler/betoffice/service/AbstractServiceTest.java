package de.winkler.betoffice.service;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Abstract helper class to init the spring test configuration.
 * 
 * @author Andre Winkler
 */
@SpringJUnitConfig(locations = { "/betoffice-test-properties.xml",
        "/betoffice.xml" })
public abstract class AbstractServiceTest {

}
