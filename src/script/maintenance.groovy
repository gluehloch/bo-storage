@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')
@Grab(group='commons-pool', module='commons-pool', version='1.5.4')
@Grab(group='xml-apis', module='xml-apis', version='1.0.b2')
@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='1.1.1-SNAPSHOT')

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.BeanFactory

import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport.DataLoader


try {
	def mysql = new MySqlDatabasedTestSupport()
	def context = new ClassPathXmlApplicationContext(
	    ['/betoffice-persistence.xml', '/hibernate-mysql-test.xml'] as String[])
	def maintenanceService = context.getBean('databaseMaintenanceService')
} catch (Exception ex) {
    ex.printStackTrace()
}

