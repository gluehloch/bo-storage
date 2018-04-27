// groovy -c UTF-8 wm2018.groovy
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

import de.winkler.betoffice.storage.*
import de.winkler.betoffice.storage.enums.*

def context = new ClassPathXmlApplicationContext(
    ['classpath:/betoffice-persistence.xml', 'classpath:/betoffice-datasource.xml', 'file:hibernate.xml'] as String[])
def maintenanceService = context.getBean('databaseMaintenanceService')
def master = context.getBean('masterDataManagerService');
def seasonService = context.getBean('seasonManagerService')


def seasonOptional = seasonService.findSeasonByName('WM Russland', '2018')
def season = seasonOptional.get()
if (seasonOptional.present) {
    season = seasonOptional.get()
} else {
    def wm2018 = new Season();
    wm2018.name = 'WM Russland'
    wm2018.year = 2018
    wm2018.mode = SeasonType.WC
    wm2018.teamType = TeamType.FIFA
    def wm2018_ = seasonService.createSeason(wm2018);
    season = wm2018_
}

print season.name + " - " + season.year

def validate(object) {
    if (object == null) {
        throw new NullPointerException();
    } else {
        println object
    }
}

 // def bundesliga = master.findGroupType('1. Bundesliga');
def gruppeA = master.findGroupType('Gruppe A').get();
validate(gruppeA)
def gruppeB = master.findGroupType('Gruppe B').get();
validate(gruppeB)
def gruppeC = master.findGroupType('Gruppe C').get();
validate(gruppeC)
def gruppeD = master.findGroupType('Gruppe D').get();
validate(gruppeD)
def gruppeE = master.findGroupType('Gruppe E').get();
validate(gruppeE)
def gruppeF = master.findGroupType('Gruppe F').get();
validate(gruppeF)
def gruppeG = master.findGroupType('Gruppe G').get();
validate(gruppeG)
def gruppeH = master.findGroupType('Gruppe H').get();
validate(gruppeH)
def achtelfinale = master.findGroupType('Achtelfinale').get();
validate(achtelfinale)
def viertelfinale = master.findGroupType('Viertelfinale').get();
validate(viertelfinale)
def halbfinale = master.findGroupType('Halbfinale').get();
validate(halbfinale)
def finale = master.findGroupType('Finale').get();
validate(finale)
def platz3 = master.findGroupType('Spiel um Platz 3').get();
validate(platz3)

def oesterreich = master.findTeam('Österreich').get()
println oesterreich

def aegypten = master.findTeam('Ägypten')
if (!aegypten.present) {
    def team = new Team()
    team.name = 'Ägypten'
    team.longName = 'Ägypten'
    team.shortName = 'Ägypten'
    team.xshortName = 'AGP'
    team.logo = 'aegypten.gif'
    team.teamType = TeamType.FIFA
    master.updateTeam(team)
    aegypten = team
} else {
    aegypten = aegypten.get()
}
validate(aegypten)

def argentinien = master.findTeam('Argentinien').get();
validate(argentinien)
def australien = master.findTeam('Australien').get();
validate(australien)
def belgien = master.findTeam('Belgien').get();
validate(belgien)

def brasilien = master.findTeam('Brasilien');
def costaRica = master.findTeam('Costa Rica');
def daenemark = master.findTeam('Dänemark');
def uruguay = master.findTeam('Uruguay');

def deutschland = master.findTeam('Deutschland');
def england = master.findTeam('England');
def frankreich = master.findTeam('Frankreich');
def iran = master.findTeam('Iran');

def island = master.findTeam('Island');
def japan = master.findTeam('Japan');
def kolumbien = master.findTeam('Kolumbien');
def kroatien = master.findTeam('Kroatien');

def marokko = master.findTeam('Marokko');
def mexiko = master.findTeam('Mexiko');
def nigeria = master.findTeam('Nigeria');
def panama = master.findTeam('Panama');

def peru = master.findTeam('Peru');
def polen = master.findTeam('Polen');
def portugal = master.findTeam('Portugal');
def russland = master.findTeam('Russland');
 
def saudiArabien = master.findTeam('Saudi Arabien');
def schweden = master.findTeam('Schweden');
def schweiz = master.findTeam('Schweiz');
def senegal = master.findTeam('Senegal');

def serbien = master.findTeam('Serbien');
def spanien = master.findTeam('Spanien');
def suedkorea = master.findTeam('Südkorea');
def tunesien = master.findTeam('Tunesien');

/*
def a = season.addGroupType(wm2018, gruppeA);
def b = season.addGroupType(wm2018, gruppeB);
def c = season.addGroupType(wm2018, gruppeC);
def d = season.addGroupType(wm2018, gruppeD);
def e = season.addGroupType(wm2018, gruppeE);
def f = season.addGroupType(wm2018, gruppeF);
def g = season.addGroupType(wm2018, gruppeG);
def h = season.addGroupType(wm2018, gruppeH);
*/

/*
season.addTeam(wm2018, a, russland);
season.addTeam(wm2018, a, saudiArabien);
season.addTeam(wm2018, a, uruguay);
season.addTeam(wm2018, a, aegypten);

with (wm2018) do {
    addTeam a, russland, saudiArabien, uruguay, aegypten;
}

season.addTeam(wm2018, b, marokko);
season.addTeam(wm2018, b, iran);
season.addTeam(wm2018, b, spanien);
season.addTeam(wm2018, b, portugal);
season.addTeam(wm2018, c, frankreich);
season.addTeam(wm2018, c, peru);
season.addTeam(wm2018, c, australien);
season.addTeam(wm2018, c, daenemark);
season.addTeam(wm2018, d, argentinien);
season.addTeam(wm2018, d, island);
season.addTeam(wm2018, d, kroatien);
season.addTeam(wm2018, d, nigeria);
season.addTeam(wm2018, e, costaRica);
season.addTeam(wm2018, e, serbien);
season.addTeam(wm2018, e, brasilien);
season.addTeam(wm2018, e, schweiz);
season.addTeam(wm2018, f, deutschland);
season.addTeam(wm2018, f, mexiko);
season.addTeam(wm2018, f, schweden);
season.addTeam(wm2018, f, suedkorea);
season.addTeam(wm2018, g, belgien);
season.addTeam(wm2018, g, panama);
season.addTeam(wm2018, g, tunesien);
season.addTeam(wm2018, g, england);
season.addTeam(wm2018, h, kolumbien);
season.addTeam(wm2018, h, japan);
season.addTeam(wm2018, h, polen);
season.addTeam(wm2018, h, senegal);
*/
