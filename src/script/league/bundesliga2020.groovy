// groovy -c UTF-8 bundesliga2019.groovy
// Copy the current JAR to the grape repository:
// cp ./target/betoffice-storage-2.6.0-SNAPSHOT.jar \
//   /cygdrive/c/Users/winkler/.groovy/grapes/de.winkler.betoffice/betoffice-storage/jars/betoffice-storage-2.6.0-SNAPSHOT.jar
// @GrabResolver(name='gluehloch', root='https://maven.gluehloch.de/repository')
// @GrabResolver(name='maven', root='https://mvnrepository.com/artifact/')
// @GrabResolver(name='apache', root='http://repo1.maven.org/maven2')
@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')

// Die naechsten 4 Imports kann man vielleicht mal in Frage stellen.
@Grab(group='javax.activation', module='activation', version='1.1')
@Grab(group='commons-logging', module='commons-logging', version='1.2')
@Grab(group='dom4j', module='dom4j', version='1.6.1')
@Grab(group='jaxen', module='jaxen', version='1.1')
@Grab(group='joda-time', module='joda-time', version='2.10')
@Grab(group='org.hibernate', module='hibernate-core', version='5.4.1.Final')

//@Grab(group='org.springframework', module='spring-core', version='5.1.5.RELEASE')
//@Grab(group='org.springframework', module='spring-context', version='5.1.5.RELEASE')

@Grab(group='org.apache.commons', module='commons-pool2', version='2.8.1')
@Grab(group='org.apache.commons', module='commons-dbcp2', version='2.7.0')
@Grab(group='org.mariadb.jdbc', module='mariadb-java-client', version='2.6.2')
@Grab(group='xml-apis', module='xml-apis', version='1.0.b2')

@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.7.1', transitive=true)
// @Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0')
// @Grab(group='de.betoffice', module='betoffice-openligadb', version='1.5.5')

import org.springframework.context.support.ClassPathXmlApplicationContext

import java.time.*
import java.time.format.*

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
        // def europeBerlinZone = ZoneId.of("Europe/Berlin")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        // formatter.withZone(europeBerlinZone)
        ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeAsString, formatter);

        // Joda Date Time Converter
        // DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        // DateTime dateTime = formatter.parseDateTime(dateTimeAsString)
        return dateTime
    }

    def createSeason(season) {
        seasonService.createSeason(season)
    }

    def updateSeason(season) {
        seasonService.updateSeason(season)
    }

    def findGroupType(groupType) {
        return masterService.findGroupType(groupType).get()
    }

    def findRound(season, index) {
        return seasonService.findRound(season, index)
    }

    def findRoundGames(roundId) {
        return seasonService.findRoundGames(roundId).orElseThrow()
    }

    def findGroup(season, groupType) {
        try {
            return seasonService.findGroup(season, groupType)
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
        return seasonService.addTeams(season, groupType, teams)
    }

    def addRound(season, date, groupType) {
        return seasonService.addRound(season, toDate(date), groupType)
    }

    def addMatch(round, date, group, homeTeam, guestTeam) {
      return seasonService.addMatch(round, toDate(date), group, homeTeam, guestTeam)
    }

    def updateMatch(game) {
        seasonService.updateMatch(game)
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
def seasonOptional = service.seasonService.findSeasonByName('Bundesliga', '2021/2022')
if (seasonOptional.isPresent()) {
    bundesliga = seasonOptional.get()
    def championshipConfiguration = new ChampionshipConfiguration()
    championshipConfiguration.openligaLeagueShortcut = 'bl1'
    championshipConfiguration.openligaLeagueSeason = '2021'
    bundesliga.setChampionshipConfiguration(championshipConfiguration)
    service.updateSeason(bundesliga)
} else {
    bundesliga = new Season();
    bundesliga.name = 'Bundesliga'
    bundesliga.year = '2021/2022'
    bundesliga.mode = SeasonType.LEAGUE
    bundesliga.teamType = TeamType.DFB
    bundesliga.getChampionshipConfiguration().openligaLeagueShortcut = 'bl1'
    bundesliga.getChampionshipConfiguration().openligaLeagueSeason = '2021'
    bundesliga = service.createSeason(bundesliga)
}

println bundesliga.name + " - " + bundesliga.year

def bundesligaGroupType = service.findGroupType('1. Liga');
validate bundesligaGroupType

/*
if (!unionBerlin.present) {
    def team = new Team()
    team.name = 'Union Berlin'
    team.longName = '1.FC Union Berlin'
    team.shortName = 'Union Berlin'
    team.xshortName = 'UNI'
    team.logo = 'unionberlin.gif'
    team.teamType = TeamType.DFB
    service.updateTeam(team)
    unionBerlin = team
} else {
    unionBerlin = unionBerlin.get()
}
*/

def unionBerlin = service.findTeam('Union Berlin').get();
validate unionBerlin

def augsburg = service.findTeam('FC Augsburg').get();
validate augsburg

def herthaBSC = service.findTeam('Hertha BSC Berlin').get();
validate herthaBSC

def arminiaBielefeld = service.findTeam('Arminia Bielefeld').get();
validate arminiaBielefeld

def werderBremen = service.findTeam('SV Werder Bremen').get();
validate werderBremen

def borussiaDortmund = service.findTeam('Borussia Dortmund').get();
validate borussiaDortmund

def eintrachtFrankfurt = service.findTeam('Eintracht Frankfurt').get();
validate eintrachtFrankfurt

def scFreiburg = service.findTeam('SC Freiburg').get();
validate scFreiburg

def tsgHoffenheim = service.findTeam('1899 Hoffenheim').get();
validate tsgHoffenheim

def fcKoeln = service.findTeam('1.FC Köln').get();
validate fcKoeln

def rbLeipzig = service.findTeam('RB Leipzig').get();
validate rbLeipzig

def bayer04Leverkusen = service.findTeam('Bayer 04 Leverkusen').get();
validate bayer04Leverkusen

def fsvMainz05 = service.findTeam('FSV Mainz 05').get();
validate fsvMainz05

def borussaGladbach = service.findTeam('Borussia MGladbach').get();
validate borussaGladbach

def bayernMuenchen = service.findTeam('FC Bayern München').get();
validate bayernMuenchen

def schalke = service.findTeam('FC Schalke 04').get();
validate schalke

def vfbStuttgart = service.findTeam('VfB Stuttgart').get();
validate vfbStuttgart

def vflWolfsburg = service.findTeam('VfL Wolfsburg').get();
validate vflWolfsburg

/*
def fortunaDuesseldorf = service.findTeam('Fortuna Düsseldorf').get();
validate fortunaDuesseldorf

def paderborn = service.findTeam('SC Paderborn 07').get();
validate paderborn

def hannover96 = service.findTeam('Hannover 96').get();
validate hannover96

def fcNuernberg = service.findTeam('1.FC Nürnberg').get();
validate fcNuernberg
*/

def bundesliga2020group = service.addGroup bundesliga, bundesligaGroupType
bundesliga2020group = service.findGroup(bundesliga, bundesligaGroupType)
println "Gruppe Bundesliga 2021/2022: $bundesliga2020group"

bundesliga2020group = service.addTeams(bundesliga, bundesligaGroupType, [
    unionBerlin,
    augsburg,
    herthaBSC,
    arminiaBielefeld,
    werderBremen,
    borussiaDortmund,
    eintrachtFrankfurt,
    scFreiburg,
    tsgHoffenheim,
    fcKoeln,
    rbLeipzig,
    bayer04Leverkusen,
    fsvMainz05,
    borussaGladbach,
    bayernMuenchen,
    schalke,
    vfbStuttgart,
    vflWolfsburg
])


def round_2020_09_18 = service.findRound(bundesliga, 0)
if (round_2020_09_18.isPresent()) {
    round_2020_09_18 = round_2020_09_18.get()
} else {
    round_2020_09_18 = service.addRound(bundesliga, '2020-09-18 20:30:00 +02:00', bundesligaGroupType)
}
println "Runde $round_2020_09_18.dateTime"

def game = service.addMatch(round_2020_09_18, '2020-09-18 20:30:00 +02:00', bundesliga2020group, bayernMuenchen, schalke)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 15:30:00 +02:00', bundesliga2020group, eintrachtFrankfurt, arminiaBielefeld)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 15:30:00 +02:00', bundesliga2020group, unionBerlin, augsburg)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 15:30:00 +02:00', bundesliga2020group, fcKoeln, tsgHoffenheim)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 15:30:00 +02:00', bundesliga2020group, werderBremen, herthaBSC)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 15:30:00 +02:00', bundesliga2020group, vfbStuttgart, scFreiburg)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-19 18:30:00 +02:00', bundesliga2020group, borussiaDortmund, borussaGladbach)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-20 15:30:00 +02:00', bundesliga2020group, rbLeipzig, fsvMainz05)
println game.index
service.updateMatch(game)

game = service.addMatch(round_2020_09_18, '2020-09-20 18:00:00 +02:00', bundesliga2020group, vflWolfsburg, bayer04Leverkusen)
println game.index
service.updateMatch(game)
