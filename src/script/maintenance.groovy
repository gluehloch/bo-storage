@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')
@Grab(group='commons-pool', module='commons-pool', version='1.5.4')
@Grab(group='commons-dbcp', module='commons-dbcp', version='1.4')
@Grab(group='mysql', module='mysql-connector-java', version='5.1.31')
@Grab(group='xml-apis', module='xml-apis', version='1.0.b2')

@Grab(group='de.awtools', module='awtools-basic', version='1.4.0-SNAPSHOT')
@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.5.1')

import org.springframework.context.support.ClassPathXmlApplicationContext

try {
	def context = new ClassPathXmlApplicationContext(
	    ['classpath:/betoffice-persistence.xml', 'classpath:/betoffice-datasource.xml', 'file:hibernate.xml'] as String[])
	def maintenanceService = context.getBean('databaseMaintenanceService')
	def seasonService = context.getBean('seasonManagerService')

	def seasonOptional = seasonService.findSeasonByName('Fussball Bundesliga', '2016/2017')
	def season = seasonOptional.get()
	print season.name + " - " + season.year
} catch (Exception ex) {
    ex.printStackTrace()
}

