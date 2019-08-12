// groovy -c UTF-8 bundesliga2019.groovy
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
// @Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0')
// @Grab(group='de.betoffice', module='betoffice-openligadb', version='1.5.5')

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
    // def openligadbUpdateService

    public Service() {
        context = new ClassPathXmlApplicationContext(
            ['classpath:/betoffice-persistence.xml', 'classpath:/betoffice-datasource.xml', 'file:hibernate.xml'] as String[])
        maintenanceService = context.getBean('databaseMaintenanceService')
        masterService = context.getBean('masterDataManagerService')
        seasonService = context.getBean('seasonManagerService')
        // openligadbUpdateService = context.getBean('openligadbUpdateService')
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

def bundesliga
def seasonOptional = service.seasonService.findSeasonByName('Fussball Bundesliga', '2019/2020')
if (seasonOptional.isPresent()) {
    bundesliga = seasonOptional.get()
} else {
    throw new IllegalStateException();
    /*
    bundesliga = new Season();
    bundesliga.name = 'Bundesliga'
    bundesliga.year = 2018
    bundesliga.mode = SeasonType.LEAGUE
    bundesliga.teamType = TeamType.DFB
    bundesliga = service.createSeason(bundesliga);
    */
}

println bundesliga.name + " - " + bundesliga.year

def bundesligaGroupType = service.findGroupType('1. Liga');
validate bundesligaGroupType


def herthaBSC = service.findTeam('Hertha BSC Berlin').get();
validate herthaBSC

def bayernMuenchen = service.findTeam('FC Bayern München').get();
validate bayernMuenchen

def werderBremen = service.findTeam('SV Werder Bremen').get();
validate werderBremen

def fortunaDuesseldorf = service.findTeam('Fortuna Düsseldorf').get();
validate fortunaDuesseldorf

def scFreiburg = service.findTeam('SC Freiburg').get();
validate scFreiburg

def fsvMainz05 = service.findTeam('FSV Mainz 05').get();
validate fsvMainz05

def bayer04Leverkusen = service.findTeam('Bayer 04 Leverkusen').get();
validate bayer04Leverkusen

def paderborn = service.findTeam('SC Paderborn 07').get();
validate paderborn

def borussiaDortmund = service.findTeam('Borussia Dortmund').get();
validate borussiaDortmund

def augsburg = service.findTeam('FC Augsburg').get();
validate augsburg

def vflWolfsburg = service.findTeam('VfL Wolfsburg').get();
validate vflWolfsburg

def fcKoeln = service.findTeam('1.FC Köln').get();
validate fcKoeln

def borussaGladbach = service.findTeam('Borussia MGladbach').get();
validate borussaGladbach

def schalke = service.findTeam('FC Schalke 04').get();
validate schalke

def eintrachtFrankfurt = service.findTeam('Eintracht Frankfurt').get();
validate eintrachtFrankfurt

def tsgHoffenheim = service.findTeam('1899 Hoffenheim').get();
validate tsgHoffenheim

def unionBerlin = service.findTeam('Union Berlin').get();
validate unionBerlin

def rbLeipzig = service.findTeam('RB Leipzig').get();
validate rbLeipzig

/*
def hannover96 = service.findTeam('Hannover 96').get();
validate hannover96

def fcNuernberg = service.findTeam('1.FC Nürnberg').get();
validate fcNuernberg

def vfbStuttgart = service.findTeam('VfB Stuttgart').get();
validate vfbStuttgart
*/

def bundesliga2018group = service.addGroup bundesliga, bundesligaGroupType
bundesliga = service.findRoundGroupTeamUserRelations(bundesliga)
// println "Gruppe Bundesliga 2018: $bundesliga2018group"

bundesliga2018group = service.addTeams(bundesliga, bundesligaGroupType, [
    augsburg,
    herthaBSC,
    vflWolfsburg,
    schalke,
    bayernMuenchen,
    borussaGladbach,
    fsvMainz05,
    bayer04Leverkusen,
    rbLeipzig,
    tsgHoffenheim,
    werderBremen,
    borussiaDortmund,
    fortunaDuesseldorf,
    eintrachtFrankfurt,
    scFreiburg,
    paderborn,
    unionBerlin,
    fcKoeln
])

/*
def round_2018_08_25 = service.findRound(bundesliga, 0)
if (round_2018_08_25.isPresent()) {
    round_2018_08_25 = round_2018_08_25.get()
} else {
    round_2018_08_25 = service.addRound(bundesliga, '2018-08-24 20:30:00', bundesligaGroupType)
}
println "Runde $round_2018_08_25.dateTime"


service.addMatch(round_2018_08_25, '2018-08-24 20:30:00', bundesliga2018group, bayernMuenchen, tsgHoffenheim)

service.addMatch(round_2018_08_25, '2018-08-25 15:30:00', bundesliga2018group, herthaBSC, fcNuernberg)
service.addMatch(round_2018_08_25, '2018-08-25 15:30:00', bundesliga2018group, werderBremen, hannover96)
service.addMatch(round_2018_08_25, '2018-08-25 15:30:00', bundesliga2018group, scFreiburg, eintrachtFrankfurt)
service.addMatch(round_2018_08_25, '2018-08-25 15:30:00', bundesliga2018group, vflWolfsburg, schalke)
service.addMatch(round_2018_08_25, '2018-08-25 15:30:00', bundesliga2018group, fortunaDuesseldorf, augsburg)
service.addMatch(round_2018_08_25, '2018-08-25 18:30:00', bundesliga2018group, borussaGladbach, bayer04Leverkusen)

service.addMatch(round_2018_08_25, '2018-08-26 15:30:00', bundesliga2018group, fsvMainz05, vfbStuttgart)
service.addMatch(round_2018_08_25, '2018-08-26 18:00:00', bundesliga2018group, borussiaDortmund, rbLeipzig)
*/
