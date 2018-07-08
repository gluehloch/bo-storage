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

def viertelfinale = service.findGroupType('Viertelfinale');
validate viertelfinale

def halbfinale = service.findGroupType('Halbfinale');
validate halbfinale

def finale = service.findGroupType('Finale');
validate finale

def platz3 = service.findGroupType('Spiel um Platz 3');
validate platz3

/*
def viertelfinale = service.findGroupType('Viertelfinale');
validate viertelfinale
*/

def oesterreich = service.findTeam('Österreich').get()
println oesterreich

def aegypten = service.findTeam('Ägypten')
if (!aegypten.present) {
    def team = new Team()
    team.name = 'Ägypten'
    team.longName = 'Ägypten'
    team.shortName = 'Ägypten'
    team.xshortName = 'AGP'
    team.logo = 'aegypten.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
    aegypten = team
} else {
    aegypten = aegypten.get()
}
validate aegypten

def argentinien = service.findTeam('Argentinien').get();
validate argentinien
def australien = service.findTeam('Australien').get();
validate australien
def belgien = service.findTeam('Belgien').get();
validate belgien

def brasilien = service.findTeam('Brasilien').get();
validate brasilien
def costaRica = service.findTeam('Costa Rica').get();
validate costaRica
def daenemark = service.findTeam('Dänemark').get();
validate daenemark
def uruguay = service.findTeam('Uruguay').get();
validate uruguay

def deutschland = service.findTeam('Deutschland').get();
validate deutschland
def england = service.findTeam('England').get();
validate england
def frankreich = service.findTeam('Frankreich').get();
validate frankreich
def iran = service.findTeam('Iran').get();
validate iran

def island = service.findTeam('Island').get();
validate island
def japan = service.findTeam('Japan').get();
validate japan
def kolumbien = service.findTeam('Kolumbien').get();
validate kolumbien
def kroatien = service.findTeam('Kroatien').get();
validate kroatien

def marokko = service.findTeam('Marokko')
if (!marokko.present) {
    def team = new Team()
    team.name = 'Marokko'
    team.longName = 'Marokko'
    team.shortName = 'Marokko'
    team.xshortName = 'MRK'
    team.logo = 'marokko.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
    marokko = team
} else {
    marokko = marokko.get()
}
validate marokko

def mexiko = service.findTeam('Mexiko').get();
validate mexiko
def nigeria = service.findTeam('Nigeria').get();
validate nigeria

def panama = service.findTeam 'Panama'
if (!panama.present) {
    def team = new Team()
    team.name = 'Panama'
    team.longName = 'Panama'
    team.shortName = 'Panama'
    team.xshortName = 'PAN'
    team.logo = 'panama.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
    panama = team
} else {
    panama = panama.get()
}
validate panama

def peru = service.findTeam 'Peru';
if (!peru.present) {
    def team = new Team()
    team.name = 'Peru'
    team.longName = 'Peru'
    team.shortName = 'Peru'
    team.xshortName = 'PRU'
    team.logo = 'peru.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
    peru = team
} else {
    peru = peru.get()
}
validate peru

def polen = service.findTeam('Polen').get();
validate polen
def portugal = service.findTeam('Portugal').get();
validate portugal
def russland = service.findTeam('Russland').get();
validate russland

def saudiArabien = service.findTeam('Saudi Arabien').get();
validate saudiArabien
def schweden = service.findTeam('Schweden').get();
validate schweden
def schweiz = service.findTeam('Schweiz').get();
validate schweiz
def senegal = service.findTeam('Senegal').get();
validate senegal

def serbien = service.findTeam('Serbien').get();
validate serbien
def spanien = service.findTeam('Spanien').get();
validate spanien
def suedkorea = service.findTeam('Rep.Korea').get();
validate suedkorea
def tunesien = service.findTeam('Tunesien').get();
validate tunesien


def wm2018_halbfinale = service.addGroup wm2018, halbfinale
println "Halbfinale: $wm2018_halbfinale.id"

wm2018 = service.findRoundGroupTeamUserRelations(wm2018)

wm2018_viertelfinale = service.addTeams(wm2018, halbfinale, [
    frankreich, belgien, england, kroatien
])
printTeams(wm2018_halbfinale)


def round_2018_07_10 = service.findRound(wm2018, 21)
if (round_2018_07_10.isPresent()) {
    round_2018_07_10 = round_2018_07_10.get()
} else {
    round_2018_07_10 = service.addRound(wm2018, '2018-07-10 20:00:00', halbfinale)
}
println "Runde $round_2018_07_10.dateTime"

def round_2018_07_11 = service.findRound(wm2018, 22)
if (round_2018_07_11.isPresent()) {
    round_2018_07_11 = round_2018_07_11.get()
} else {
    round_2018_07_11 = service.addRound(wm2018, '2018-07-11 20:00:00', halbfinale)
}
println "Runde $round_2018_07_11.dateTime"


service.addMatch(round_2018_07_10, '2018-07-10 20:00:00', wm2018_halbfinale, frankreich, belgien)
service.addMatch(round_2018_07_11, '2018-07-11 20:00:00', wm2018_halbfinale, kroatien, england)
