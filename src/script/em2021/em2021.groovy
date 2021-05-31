// groovy -c UTF-8 wm2018.groovy
// Copy the current JAR to the grape repository:
// cp ./target/betoffice-storage-2.6.0-SNAPSHOT.jar \
//   /cygdrive/c/Users/winkler/.groovy/grapes/de.winkler.betoffice/betoffice-storage/jars/betoffice-storage-2.6.0-SNAPSHOT.jar

@GrabResolver(name='gluehloch', root='http://maven.gluehloch.de/repository')
@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')

// Die naechsten 4 Imports kann man vielleicht mal in Frage stellen.
// @Grab(group='javax.activation', module='activation', version='1.1')
// @Grab(group='commons-logging', module='commons-logging', version='1.2')
// @Grab(group='dom4j', module='dom4j', version='1.6.1')
// @Grab(group='jaxen', module='jaxen', version='1.1')

@Grab(group='org.apache.commons', module='commons-pool2', version='2.8.1')
@Grab(group='org.apache.commons', module='commons-dbcp2', version='2.7.0')
@Grab(group='org.mariadb.jdbc', module='mariadb-java-client', version='2.6.2')
// @Grab(group='xml-apis', module='xml-apis', version='1.0.b2')

/// @Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0-SNAPSHOT')
/// @Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.7.2')
@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.8.0-SNAP-2021-05-31')

import org.springframework.context.support.ClassPathXmlApplicationContext

import java.time.*;
import java.time.format.*;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeAsString);
        /*
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        DateTime dateTime = formatter.parseDateTime(dateTimeAsString)
        return dateTime
        */
        return zonedDateTime
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
        def team = masterService.findTeam(teamName)
        if (!team.isPresent()) {
            println "team '${teamName}' not found"
        } else {
            println "Team '${team.get().getName()}' is there."
        }
        return team
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

def em2021
def seasonOptional = service.seasonService.findSeasonByName('EM Europe', '2021')
if (seasonOptional.isPresent()) {
    em2021 = seasonOptional.get()
} else {
    em2021 = new Season();
    em2021.name = 'EM Europe'
    em2021.year = 2021
    em2021.mode = SeasonType.EC
    em2021.teamType = TeamType.FIFA
    em2021 = service.createSeason(em2021);
}

println em2021.name + " - " + em2021.year

def newTeam
newTeam = service.findTeam('Finnland')
if (!newTeam.present) {
    def team = new Team()
    team.name = 'Finnland'
    team.longName = 'Finnland'
    team.shortName = 'Finnland'
    team.xshortName = 'FIN'
    team.logo = 'finnland.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
}

newTeam = service.findTeam('Nordmazedonien')
if (!newTeam.present) {
    def team = new Team()
    team.name = 'Nordmazedonien'
    team.longName = 'Nordmazedonien'
    team.shortName = 'Nordmazedonien'
    team.xshortName = 'MKD'
    team.logo = 'nordmazedonien.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
}

newTeam = service.findTeam('Schottland')
if (!newTeam.present) {
    def team = new Team()
    team.name = 'Schottland'
    team.longName = 'Schottland'
    team.shortName = 'Schottland'
    team.xshortName = 'SCO'
    team.logo = 'schottland.gif'
    team.teamType = TeamType.FIFA
    service.updateTeam(team)
}

 // def bundesliga = master.findGroupType('1. Bundesliga');
def gruppeA = service.findGroupType('Gruppe A');
validate gruppeA
def gruppeB = service.findGroupType('Gruppe B');
validate gruppeB
def gruppeC = service.findGroupType('Gruppe C');
validate gruppeC
def gruppeD = service.findGroupType('Gruppe D');
validate gruppeD
def gruppeE = service.findGroupType('Gruppe E');
validate gruppeE
def gruppeF = service.findGroupType('Gruppe F');
validate gruppeF

/*
def achtelfinale = service.findGroupType('Achtelfinale');
validate achtelfinale
def viertelfinale = service.findGroupType('Viertelfinale');
validate viertelfinale
def halbfinale = service.findGroupType('Halbfinale');
validate halbfinale
def finale = service.findGroupType('Finale');
validate finale
def platz3 = service.findGroupType('Spiel um Platz 3');
validate platz3
*/

// Gruppe A
def italien = service.findTeam('Italien').get()
validate italien

def schweiz = service.findTeam('Schweiz').get();
validate schweiz

def tuerkei = service.findTeam("Türkei").get()
validate tuerkei

def wales = service.findTeam('Wales').get();
validate wales

// Gruppe B
def belgien = service.findTeam('Belgien').get();
validate belgien

def daenemark = service.findTeam('Dänemark').get();
validate daenemark

def finnland = service.findTeam('Finnland').get();
validate finnland

def russland = service.findTeam('Russland').get();
validate russland

// Gruppe C
def niederlande = service.findTeam('Niederlande').get();
validate niederlande

def nordmazedonien = service.findTeam('Nordmazedonien').get();
validate nordmazedonien

def oesterreich = service.findTeam('Österreich').get()
validate oesterreich

def ukraine = service.findTeam('Ukraine').get();
validate ukraine

// Gruppe D
def england = service.findTeam('England').get();
validate england

def kroatien = service.findTeam('Kroatien').get();
validate kroatien

def schottland = service.findTeam('Schottland').get();
validate schottland

def tschechien = service.findTeam('Tschechien').get();
validate tschechien

// Gruppe E
def polen = service.findTeam('Polen').get();
validate polen

def schweden = service.findTeam('Schweden').get();
validate schweden

def slowakei = service.findTeam('Slowakei').get();
validate slowakei

def spanien = service.findTeam('Spanien').get();
validate spanien

// Gruppe F
def deutschland = service.findTeam('Deutschland').get();
validate deutschland

def frankreich = service.findTeam('Frankreich').get();
validate frankreich

def portugal = service.findTeam('Portugal').get();
validate portugal

def ungarn = service.findTeam('Ungarn').get()
validate ungarn


// TODO #addGroup liefert nicht die Group zurück, sonder die Saison.
def em2021_gruppe_A = service.addGroup em2021, gruppeA
println "Gruppe A: $em2021_gruppe_A.id"

def em2021_gruppe_B = service.addGroup em2021, gruppeB
println "Gruppe B: $em2021_gruppe_B.id"

def em2021_gruppe_C = service.addGroup em2021, gruppeC
println "Gruppe C: $em2021_gruppe_C.id"

def em2021_gruppe_D = service.addGroup em2021, gruppeD
println "Gruppe D: $em2021_gruppe_D.id"

def em2021_gruppe_E = service.addGroup em2021, gruppeE
println "Gruppe E: $em2021_gruppe_E.id"

def em2021_gruppe_F = service.addGroup em2021, gruppeF
println "Gruppe F: $em2021_gruppe_F.id"


em2021_gruppe_A = service.addTeams(em2021, gruppeA, [italien, schweiz, tuerkei, wales])
printTeams(em2021_gruppe_A)

em2021_gruppe_B = service.addTeams(em2021, gruppeB, [belgien, daenemark, finnland, russland]);
printTeams(em2021_gruppe_B)

em2021_gruppe_C = service.addTeams(em2021, gruppeC, [niederlande, nordmazedonien, oesterreich, ukraine])
printTeams(em2021_gruppe_C)

em2021_gruppe_D = service.addTeams(em2021, gruppeD, [england, kroatien, schottland, tschechien])
printTeams(em2021_gruppe_D)

em2021_gruppe_E = service.addTeams(em2021, gruppeE, [polen, schweden, slowakei, spanien])
printTeams(em2021_gruppe_E)

em2021_gruppe_F = service.addTeams(em2021, gruppeF, [deutschland, frankreich, portugal, ungarn])
printTeams(em2021_gruppe_F)

// 

def round_2021_06_11 = service.findRound(em2021, 0)
if (round_2021_06_11.isPresent()) {
    round_2021_06_11 = round_2021_06_11.get()
} else {
    round_2021_06_11 = service.addRound(em2021, '2021-06-11 21:00:00', gruppeA)
}
println "Runde $round_2021_06_11.dateTime"


def round_2021_06_16 = service.findRound(em2021, 1)
if (round_2021_06_16.isPresent()) {
    round_2021_06_16 = round_2021_06_16.get()
} else {
    round_2021_06_16 = service.addRound(wm2018, '2021-06-16 14:00:00', gruppeA)
}
println "Runde $round_2021_06_16.dateTime"


def round_2021_06_20 = service.findRound(em2021, 3)
if (round_2021_06_20.isPresent()) {
    round_2021_06_20 = round_2021_06_20.get()
} else {
    round_2021_06_20 = service.addRound(wm2018, '2021-06-20 18:00:00', gruppeA)
}
println "Runde $round_2021_06_20.dateTime"

/*
def round_2018_06_17 = service.findRound(em2021, 3)
if (round_2018_06_17.isPresent()) {
    round_2018_06_17 = round_2018_06_17.get()
} else {
    round_2018_06_17 = service.addRound(wm2018, '2018-06-17 14:00:00', gruppeD)
}
println "Runde $round_2018_06_17.dateTime"

def round_2018_06_18 = service.findRound(em2021, 4)
if (round_2018_06_18.isPresent()) {
    round_2018_06_18 = round_2018_06_18.get()
} else {
    round_2018_06_18 = service.addRound(wm2018, '2018-06-18 14:00:00', gruppeE)
}
println "Runde $round_2018_06_18.dateTime"

def round_2018_06_19 = service.findRound(wm2018, 5)
if (round_2018_06_19.isPresent()) {
    round_2018_06_19 = round_2018_06_19.get()
} else {
    round_2018_06_19 = service.addRound(wm2018, '2018-06-19 14:00:00', gruppeF)
}
println "Runde $round_2018_06_19.dateTime"

def round_2018_06_20 = service.findRound(wm2018, 6)
if (round_2018_06_20.isPresent()) {
    round_2018_06_20 = round_2018_06_20.get()
} else {
    round_2018_06_20 = service.addRound(wm2018, '2018-06-20 14:00:00', gruppeG)
}
println "Runde $round_2018_06_20.dateTime"

def round_2018_06_21 = service.findRound(wm2018, 7)
if (round_2018_06_21.isPresent()) {
    round_2018_06_21 = round_2018_06_21.get()
} else {
    round_2018_06_21 = service.addRound(wm2018, '2018-06-21 14:00:00', gruppeH)
}
println "Runde $round_2018_06_21.dateTime"

def round_2018_06_22 = service.findRound(wm2018, 8)
if (round_2018_06_22.isPresent()) {
    round_2018_06_22 = round_2018_06_22.get()
} else {
    round_2018_06_22 = service.addRound(wm2018, '2018-06-22 14:00:00', gruppeA)
}
println "Runde $round_2018_06_22.dateTime"

def round_2018_06_23 = service.findRound(wm2018, 9)
if (round_2018_06_23.isPresent()) {
    round_2018_06_23 = round_2018_06_23.get()
} else {
    round_2018_06_23 = service.addRound(wm2018, '2018-06-23 14:00:00', gruppeB)
}
println "Runde $round_2018_06_23.dateTime"

def round_2018_06_24 = service.findRound(wm2018, 10)
if (round_2018_06_24.isPresent()) {
    round_2018_06_24 = round_2018_06_24.get()
} else {
    round_2018_06_24 = service.addRound(wm2018, '2018-06-24 14:00:00', gruppeC)
}
println "Runde $round_2018_06_24.dateTime"

def round_2018_06_25 = service.findRound(wm2018, 11)
if (round_2018_06_25.isPresent()) {
    round_2018_06_25 = round_2018_06_25.get()
} else {
    round_2018_06_25 = service.addRound(wm2018, '2018-06-25 14:00:00', gruppeA)
}
println "Runde $round_2018_06_25.dateTime"

def round_2018_06_26 = service.findRound(wm2018, 12)
if (round_2018_06_26.isPresent()) {
    round_2018_06_26 = round_2018_06_26.get()
} else {
    round_2018_06_26 = service.addRound(wm2018, '2018-06-26 14:00:00', gruppeC)
}
println "Runde $round_2018_06_26.dateTime"

def round_2018_06_27 = service.findRound(wm2018, 13)
if (round_2018_06_27.isPresent()) {
    round_2018_06_27 = round_2018_06_27.get()
} else {
    round_2018_06_27 = service.addRound(wm2018, '2018-06-27 14:00:00', gruppeE)
}
println "Runde $round_2018_06_27.dateTime"

def round_2018_06_28 = service.findRound(wm2018, 14)
if (round_2018_06_28.isPresent()) {
    round_2018_06_28 = round_2018_06_28.get()
} else {
    round_2018_06_28 = service.addRound(wm2018, '2018-06-28 14:00:00', gruppeG)
}
println "Runde $round_2018_06_28.dateTime"
*/

// 1. Spieltag
service.addMatch(round_2021_06_11, '2021-06-11 21:00:00', em2021_gruppe_A, tuerkei, italien)

service.addMatch(round_2021_06_11, '2021-06-12 15:00:00', em2021_gruppe_A, wales, schweiz)
service.addMatch(round_2021_06_11, '2021-06-12 18:00:00', em2021_gruppe_B, daenemark, finnland)
service.addMatch(round_2021_06_11, '2021-06-11 21:00:00', em2021_gruppe_B, belgien, russland)

service.addMatch(round_2021_06_11, '2021-06-13 15:00:00', em2021_gruppe_D, england, kroatien)
service.addMatch(round_2021_06_11, '2021-06-13 18:00:00', em2021_gruppe_C, oesterreich, nordmazedonien)
service.addMatch(round_2021_06_11, '2021-06-13 21:00:00', em2021_gruppe_C, niederlande, ukraine)

service.addMatch(round_2021_06_11, '2021-06-14 15:00:00', em2021_gruppe_D, schottland, tschechien)
service.addMatch(round_2021_06_11, '2021-06-14 18:00:00', em2021_gruppe_E, polen, slowakei)
service.addMatch(round_2021_06_11, '2021-06-14 21:00:00', em2021_gruppe_E, spanien, schweden)

service.addMatch(round_2021_06_11, '2021-06-15 18:00:00', em2021_gruppe_F, ungarn, portugal)
service.addMatch(round_2021_06_11, '2021-06-15 21:00:00', em2021_gruppe_F, frankreich, deutschland)

// 2. Spieltag
service.addMatch(round_2021_06_16, '2021-06-16 15:00:00', em2021_gruppe_B, finnland, russland)
service.addMatch(round_2021_06_16, '2021-06-16 18:00:00', em2021_gruppe_A, tuerkei, wales)
service.addMatch(round_2021_06_16, '2021-06-16 21:00:00', em2021_gruppe_A, italien, schweiz)

service.addMatch(round_2021_06_16, '2021-06-17 15:00:00', em2021_gruppe_C, ukraine, nordmazedonien)
service.addMatch(round_2021_06_16, '2021-06-17 18:00:00', em2021_gruppe_B, daenemark, belgien)
service.addMatch(round_2021_06_16, '2021-06-17 21:00:00', em2021_gruppe_C, italien, schweiz)

service.addMatch(round_2021_06_16, '2021-06-18 15:00:00', em2021_gruppe_E, schweden, slowakei)
service.addMatch(round_2021_06_16, '2021-06-18 18:00:00', em2021_gruppe_D, kroatien, tschechien)
service.addMatch(round_2021_06_16, '2021-06-18 21:00:00', em2021_gruppe_D, england, schottland)

service.addMatch(round_2021_06_16, '2021-06-19 15:00:00', em2021_gruppe_F, ungarn, frankreich)
service.addMatch(round_2021_06_16, '2021-06-19 18:00:00', em2021_gruppe_F, portugal, deutschland)
service.addMatch(round_2021_06_16, '2021-06-19 21:00:00', em2021_gruppe_E, spanien, polen)

// 3. Spieltag
service.addMatch(round_2021_06_20, '2021-06-20 18:00:00', em2021_gruppe_A, italien, wales)
service.addMatch(round_2021_06_20, '2021-06-20 18:00:00', em2021_gruppe_A, schweiz, tuerkei)

service.addMatch(round_2021_06_20, '2021-06-21 18:00:00', em2021_gruppe_C, nordmazedonien, niederlande)
service.addMatch(round_2021_06_20, '2021-06-21 18:00:00', em2021_gruppe_C, ukraine, oesterreich)
service.addMatch(round_2021_06_20, '2021-06-21 21:00:00', em2021_gruppe_B, finnland, belgien)
service.addMatch(round_2021_06_20, '2021-06-21 21:00:00', em2021_gruppe_B, russland, daenemark)

service.addMatch(round_2021_06_20, '2021-06-22 21:00:00', em2021_gruppe_D, tschechien, england)
service.addMatch(round_2021_06_20, '2021-06-22 21:00:00', em2021_gruppe_D, kroatien, schottland)

service.addMatch(round_2021_06_20, '2021-06-23 18:00:00', em2021_gruppe_E, slowakei, spanien)
service.addMatch(round_2021_06_20, '2021-06-23 18:00:00', em2021_gruppe_E, schweden, polen)
service.addMatch(round_2021_06_20, '2021-06-23 21:00:00', em2021_gruppe_F, deutschland, ungarn)
service.addMatch(round_2021_06_20, '2021-06-23 21:00:00', em2021_gruppe_F, portugal, frankreich)