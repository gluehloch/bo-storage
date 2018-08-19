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

/// @Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0-SNAPSHOT')
@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.6.0')

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

def wm2018
def seasonOptional = service.seasonService.findSeasonByName('WM Russland', '2018')
if (seasonOptional.isPresent()) {
    wm2018 = seasonOptional.get()
} else {
    wm2018 = new Season();
    wm2018.name = 'WM Russland'
    wm2018.year = 2018
    wm2018.mode = SeasonType.WC
    wm2018.teamType = TeamType.FIFA
    wm2018 = service.createSeason(wm2018);
}

println wm2018.name + " - " + wm2018.year

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
def gruppeG = service.findGroupType('Gruppe G');
validate gruppeG
def gruppeH = service.findGroupType('Gruppe H');
validate gruppeH

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


def wm2018_gruppe_A = service.addGroup wm2018, gruppeA
println "Gruppe A: $wm2018_gruppe_A.id"

def wm2018_gruppe_B = service.addGroup wm2018, gruppeB
println "Gruppe B: $wm2018_gruppe_B.id"

def wm2018_gruppe_C = service.addGroup wm2018, gruppeC
println "Gruppe C: $wm2018_gruppe_C.id"

def wm2018_gruppe_D = service.addGroup wm2018, gruppeD
println "Gruppe D: $wm2018_gruppe_D.id"

def wm2018_gruppe_E = service.addGroup wm2018, gruppeE
println "Gruppe E: $wm2018_gruppe_E.id"

def wm2018_gruppe_F = service.addGroup wm2018, gruppeF
println "Gruppe F: $wm2018_gruppe_F.id"

def wm2018_gruppe_G = service.addGroup wm2018, gruppeG
println "Gruppe G: $wm2018_gruppe_G.id"

def wm2018_gruppe_H = service.addGroup wm2018, gruppeH
println "Gruppe H: $wm2018_gruppe_H.id"

wm2018 = service.findRoundGroupTeamUserRelations(wm2018)

wm2018_gruppe_A = service.addTeams(wm2018, gruppeA, [russland, saudiArabien, uruguay, aegypten])
printTeams(wm2018_gruppe_A)

wm2018_gruppe_B = service.addTeams(wm2018, gruppeB, [marokko, iran, spanien, portugal]);
printTeams(wm2018_gruppe_B)

wm2018_gruppe_C = service.addTeams(wm2018, gruppeC, [frankreich, peru, australien, daenemark])
printTeams(wm2018_gruppe_C)

wm2018_gruppe_D = service.addTeams(wm2018, gruppeD, [argentinien, island, kroatien, nigeria])
printTeams(wm2018_gruppe_D)

wm2018_gruppe_E = service.addTeams(wm2018, gruppeE, [costaRica, serbien, brasilien, schweiz])
printTeams(wm2018_gruppe_E)

wm2018_gruppe_F = service.addTeams(wm2018, gruppeF, [deutschland, mexiko, schweden, suedkorea])
printTeams(wm2018_gruppe_F)

wm2018_gruppe_G = service.addTeams(wm2018, gruppeG, [belgien, panama, tunesien, england])
printTeams(wm2018_gruppe_G)

wm2018_gruppe_H = service.addTeams(wm2018, gruppeH, [kolumbien, japan, polen, senegal])
printTeams(wm2018_gruppe_H)

def round_2018_06_14 = service.findRound(wm2018, 0)
if (round_2018_06_14.isPresent()) {
    round_2018_06_14 = round_2018_06_14.get()
} else {
    round_2018_06_14 = service.addRound(wm2018, '2018-06-14 17:00:00', gruppeA)
}
println "Runde $round_2018_06_14.dateTime"

def round_2018_06_15 = service.findRound(wm2018, 1)
if (round_2018_06_15.isPresent()) {
    round_2018_06_15 = round_2018_06_15.get()
} else {
    round_2018_06_15 = service.addRound(wm2018, '2018-06-15 14:00:00', gruppeB)
}
println "Runde $round_2018_06_15.dateTime"

def round_2018_06_16 = service.findRound(wm2018, 2)
if (round_2018_06_16.isPresent()) {
    round_2018_06_16 = round_2018_06_16.get()
} else {
    round_2018_06_16 = service.addRound(wm2018, '2018-06-16 14:00:00', gruppeC)
}
println "Runde $round_2018_06_16.dateTime"

def round_2018_06_17 = service.findRound(wm2018, 3)
if (round_2018_06_17.isPresent()) {
    round_2018_06_17 = round_2018_06_17.get()
} else {
    round_2018_06_17 = service.addRound(wm2018, '2018-06-17 14:00:00', gruppeD)
}
println "Runde $round_2018_06_17.dateTime"

def round_2018_06_18 = service.findRound(wm2018, 4)
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



service.addMatch(round_2018_06_14, '2018-06-14 17:00:00', wm2018_gruppe_A, russland, saudiArabien)

service.addMatch(round_2018_06_15, '2018-06-15 14:00:00', wm2018_gruppe_A, aegypten, uruguay)
service.addMatch(round_2018_06_15, '2018-06-15 17:00:00', wm2018_gruppe_B, marokko, iran)
service.addMatch(round_2018_06_15, '2018-06-15 20:00:00', wm2018_gruppe_B, portugal, spanien)

service.addMatch(round_2018_06_16, '2018-06-16 12:00:00', wm2018_gruppe_C, frankreich, australien)
service.addMatch(round_2018_06_16, '2018-06-16 15:00:00', wm2018_gruppe_D, argentinien, island)
service.addMatch(round_2018_06_16, '2018-06-16 18:00:00', wm2018_gruppe_C, peru, daenemark)
service.addMatch(round_2018_06_16, '2018-06-16 21:00:00', wm2018_gruppe_D, kroatien, nigeria)

service.addMatch(round_2018_06_17, '2018-06-17 14:00:00', wm2018_gruppe_E, costaRica, serbien)
service.addMatch(round_2018_06_17, '2018-06-17 17:00:00', wm2018_gruppe_F, deutschland, mexiko)
service.addMatch(round_2018_06_17, '2018-06-17 20:00:00', wm2018_gruppe_E, brasilien, schweiz)

service.addMatch(round_2018_06_18, '2018-06-18 14:00:00', wm2018_gruppe_F, schweden, suedkorea)
service.addMatch(round_2018_06_18, '2018-06-18 17:00:00', wm2018_gruppe_G, belgien, panama)
service.addMatch(round_2018_06_18, '2018-06-18 20:00:00', wm2018_gruppe_G, tunesien, england)

service.addMatch(round_2018_06_19, '2018-06-19 14:00:00', wm2018_gruppe_H, kolumbien, japan)
service.addMatch(round_2018_06_19, '2018-06-19 17:00:00', wm2018_gruppe_H, polen, senegal)
service.addMatch(round_2018_06_19, '2018-06-19 20:00:00', wm2018_gruppe_A, russland, aegypten)

service.addMatch(round_2018_06_20, '2018-06-20 14:00:00', wm2018_gruppe_B, portugal, marokko)
service.addMatch(round_2018_06_20, '2018-06-20 17:00:00', wm2018_gruppe_A, uruguay, saudiArabien)
service.addMatch(round_2018_06_20, '2018-06-20 20:00:00', wm2018_gruppe_B, iran, spanien)

service.addMatch(round_2018_06_21, '2018-06-21 14:00:00', wm2018_gruppe_C, daenemark, australien)
service.addMatch(round_2018_06_21, '2018-06-21 17:00:00', wm2018_gruppe_C, frankreich, peru)
service.addMatch(round_2018_06_21, '2018-06-21 20:00:00', wm2018_gruppe_D, argentinien, kroatien)

service.addMatch(round_2018_06_22, '2018-06-22 14:00:00', wm2018_gruppe_E, brasilien, costaRica)
service.addMatch(round_2018_06_22, '2018-06-22 17:00:00', wm2018_gruppe_D, nigeria, island)
service.addMatch(round_2018_06_22, '2018-06-22 20:00:00', wm2018_gruppe_E, serbien, schweiz)

service.addMatch(round_2018_06_23, '2018-06-23 14:00:00', wm2018_gruppe_G, belgien, tunesien)
service.addMatch(round_2018_06_23, '2018-06-23 17:00:00', wm2018_gruppe_F, suedkorea, mexiko)
service.addMatch(round_2018_06_23, '2018-06-23 20:00:00', wm2018_gruppe_F, deutschland, schweden)

service.addMatch(round_2018_06_24, '2018-06-24 14:00:00', wm2018_gruppe_G, england, panama)
service.addMatch(round_2018_06_24, '2018-06-24 17:00:00', wm2018_gruppe_H, japan, senegal)
service.addMatch(round_2018_06_24, '2018-06-24 20:00:00', wm2018_gruppe_H, polen, kolumbien)

// Letzter Gruppenspieltag
service.addMatch(round_2018_06_25, '2018-06-25 16:00:00', wm2018_gruppe_A, uruguay, russland)
service.addMatch(round_2018_06_25, '2018-06-25 16:00:00', wm2018_gruppe_A, saudiArabien, aegypten)
service.addMatch(round_2018_06_25, '2018-06-25 20:00:00', wm2018_gruppe_B, spanien, marokko)
service.addMatch(round_2018_06_25, '2018-06-25 20:00:00', wm2018_gruppe_B, iran, portugal)

service.addMatch(round_2018_06_26, '2018-06-26 16:00:00', wm2018_gruppe_C, daenemark, frankreich)
service.addMatch(round_2018_06_26, '2018-06-26 16:00:00', wm2018_gruppe_C, australien, peru)
service.addMatch(round_2018_06_26, '2018-06-26 20:00:00', wm2018_gruppe_D, island, kroatien)
service.addMatch(round_2018_06_26, '2018-06-26 20:00:00', wm2018_gruppe_D, nigeria, argentinien)

service.addMatch(round_2018_06_27, '2018-06-27 16:00:00', wm2018_gruppe_F, mexiko, schweden)
service.addMatch(round_2018_06_27, '2018-06-27 16:00:00', wm2018_gruppe_F, suedkorea, deutschland)
service.addMatch(round_2018_06_27, '2018-06-27 20:00:00', wm2018_gruppe_E, serbien, brasilien)
service.addMatch(round_2018_06_27, '2018-06-27 20:00:00', wm2018_gruppe_E, schweiz, costaRica)

service.addMatch(round_2018_06_28, '2018-06-28 16:00:00', wm2018_gruppe_H, senegal, kolumbien)
service.addMatch(round_2018_06_28, '2018-06-28 16:00:00', wm2018_gruppe_H, japan, polen)
service.addMatch(round_2018_06_28, '2018-06-28 20:00:00', wm2018_gruppe_G, england, belgien)
service.addMatch(round_2018_06_28, '2018-06-28 20:00:00', wm2018_gruppe_G, panama, tunesien)
