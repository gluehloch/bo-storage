package de.winkler.betoffice.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Abstract helper class to init the spring test configuration.
 * 
 * @author Andre Winkler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-test-properties.xml", "/betoffice.xml" })
public abstract class AbstractServiceTest {

}
