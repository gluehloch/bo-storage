// groovy -c UTF-8 wm2018.groovy
// Copy the current JAR to the grape repository:
// cp ./target/betoffice-storage-2.6.0-SNAPSHOT.jar \
//   /cygdrive/c/Users/winkler/.groovy/grapes/de.winkler.betoffice/betoffice-storage/jars/betoffice-storage-2.6.0-SNAPSHOT.jar

@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')

// Die naechsten 4 Imports kann man vielleicht mal in Frage stellen.
@Grab(group='javax.activation', module='activation', version='1.1')
@Grab(group='commons-logging', module='commons-logging', version='1.2')
@Grab(group='dom4j', module='dom4j', version='1.6.1')
@Grab(group='jaxen', module='jaxen', version='1.1')

@Grab(group='commons-pool', module='commons-pool', version='1.5.4')
@Grab(group='commons-dbcp', module='commons-dbcp', version='1.4')
@Grab(group='mysql', module='mysql-connector-java', version='5.1.31')
@Grab(group='xml-apis', module='xml-apis', version='1.0.b2')

@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0-SNAPSHOT')

import org.springframework.context.support.ClassPathXmlApplicationContext

import org.joda.time.*;
import org.joda.time.format.*;

import de.winkler.betoffice.storage.*
import de.winkler.betoffice.storage.enums.*

class Service {
    private ClassPathXmlApplicationContext context

    def maintenanceService
    def masterService
    def seasonService

    public Service() {
        context = new ClassPathXmlApplicationContext(
            ['classpath:/betoffice-persistence.xml', 'classpath:/betoffice-datasource.xml', 'file:hibernate.xml'] as String[])
        maintenanceService = context.getBean('databaseMaintenanceService')
        masterService = context.getBean('masterDataManagerService')
        seasonService = context.getBean('seasonManagerService')
    }

    def toDate(dateTimeAsString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        DateTime dateTime = formatter.parseDateTime(dateTimeAsString)
        return dateTime
    }

    def createSeason(season) {
        seasonService.createSeason(season);
    }

    def findGroupType(groupType) {
        return masterService.findGroupType(groupType).get()
    }

    def findRoundGroupTeamUserRelations(season) {
        return seasonService.findRoundGroupTeamUserRelations(season)
    }

    def findRound(season, index) {
        return seasonService.findRound(season, index)
    }

    def findGroup(season, group) {
        try {
            return seasonService.findGroup(season, group)
        } catch (javax.persistence.NoResultException ex) {
            return null
        }
    }

    def addGroup(season, groupType) {
        def group = findGroup season, groupType
        if (group == null) {
            group = seasonService.addGroupType season, groupType
        }
        return group
    }

    def findTeam(teamName) {
        return masterService.findTeam(teamName)
    }

    def findTeams(season, groupType) {
        return seasonService.findTeams(season, groupType)
    }

    def updateTeam(team) {
        masterService.updateTeam(team)
    }

    def addTeam(season, groupType, team) {
        seasonService.addTeam(season, groupType, team)
    }

    def addTeams(season, groupType, teams) {
        seasonService.addTeams(season, groupType, teams)
    }

    def addRound(season, date, groupType) {
        return seasonService.addRound(season, toDate(date), groupType)
    }

    def addMatch(round, date, group, homeTeam, guestTeam) {
      return seasonService.addMatch(round, toDate(date), group, homeTeam, guestTeam)
    }
}

def validate(object) {
    if (object == null) {
        throw new NullPointerException();
    } else {
        println object
    }
}

def printTeams(group) {
    println "Teams $group"
    for (team in group.teams) {
        println team
    }
}

Service service = new Service();


def seasonOptional = service.seasonService.findSeasonByName('WM Russland', '2018')
def wm2018 = seasonOptional.get()

println wm2018.name + " - " + wm2018.year

def finale = service.findGroupType('Finale');
validate finale

def platz3 = service.findGroupType('Spiel um Platz 3');
validate platz3

/*
def viertelfinale = service.findGroupType('Viertelfinale');
validate viertelfinale
*/

def england = service.findTeam('England').get();
validate england
def belgien = service.findTeam('Belgien').get();
validate belgien
def frankreich = service.findTeam('Frankreich').get();
validate frankreich
def kroatien = service.findTeam('Kroatien').get();
validate kroatien



def wm2018_platz3 = service.addGroup wm2018, platz3
println "Platz 3: $wm2018_platz3.id"

def wm2018_finale = service.addGroup wm2018, finale
println "Finale: $wm2018_finale.id"

wm2018 = service.findRoundGroupTeamUserRelations(wm2018)

wm2018_platz3 = service.addTeams(wm2018, platz3, [
    belgien, england
])
printTeams(wm2018_platz3)

wm2018_finale = service.addTeams(wm2018, finale, [
    frankreich, kroatien
])
printTeams(wm2018_finale)

def round_2018_07_14 = service.findRound(wm2018, 23)
if (round_2018_07_14.isPresent()) {
    round_2018_07_14 = round_2018_07_14.get()
} else {
    round_2018_07_14 = service.addRound(wm2018, '2018-07-14 16:00:00', platz3)
}
println "Runde $round_2018_07_14.dateTime"

def round_2018_07_15 = service.findRound(wm2018, 24)
if (round_2018_07_15.isPresent()) {
    round_2018_07_15 = round_2018_07_15.get()
} else {
    round_2018_07_15 = service.addRound(wm2018, '2018-07-15 17:00:00', finale)
}
println "Runde $round_2018_07_15.dateTime"


service.addMatch(round_2018_07_14, '2018-07-14 16:00:00', wm2018_platz3, belgien, england)
service.addMatch(round_2018_07_15, '2018-07-15 17:00:00', wm2018_finale, frankreich, kroatien)
