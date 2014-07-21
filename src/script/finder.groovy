package de.winkler.betoffice.service

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.BeanFactory

import de.winkler.betoffice.database.MySqlDatabasedTestSupport
import de.winkler.betoffice.database.MySqlDatabasedTestSupport.DataLoader

/**
 * Bestimmt für Wartungsaufgaben.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class GroovyMaintenanceService {

    final def mysql
    final def context
    final def maintenanceService

    // User Frosch ID = 6, GameList = 1
    final def query = """
         select
             gametipp
         from
             GameTipp gametipp join fetch
             gametipp.user user join fetch
             gametipp.game game join fetch
             gametipp.game.gameList gameList  
        where
             gametipp.game.gameList.id = 1 and
             gametipp.game.id = game.id and
             gametipp.user.id = 6
    """ 

    GroovyMaintenanceService() {
        mysql = new MySqlDatabasedTestSupport()
        context = new ClassPathXmlApplicationContext(
                ['/betoffice-persistence.xml', '/hibernate-mysql-test.xml'] as String[])
        maintenanceService = context.getBean('databaseMaintenanceService')
    }

    def executeHql(query) {
        maintenanceService.executeHql(query)
    }

    def setupDatabase() {
        mysql.setUp(MySqlDatabasedTestSupport.DataLoader.FULL)
    }

    def tearDownDatabase() {
        mysql.deleteDatabase()
    }



    static GroovyMaintenanceService gms

    static void main(String[] args) {
        if (!gms) {
            gms = new GroovyMaintenanceService()
            gms.setupDatabase()
        }

        def result1 = gms.executeHql(gms.query)
        println result1
        def result2 = gms.executeHql(gms.query2)
        println result2

        gms.tearDownDatabase()
    }

}
