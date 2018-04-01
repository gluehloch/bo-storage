@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1')
@Grab(group='commons-pool', module='commons-pool', version='1.5.4')
@Grab(group='commons-dbcp', module='commons-dbcp', version='1.4')
@Grab(group='mysql', module='mysql-connector-java', version='5.1.31')
@Grab(group='xml-apis', module='xml-apis', version='1.0.b2')

@Grab(group='de.winkler.betoffice', module='betoffice-storage', version='2.5.2-SNAPSHOT')

import org.springframework.context.support.ClassPathXmlApplicationContext

import de.winkler.betoffice.storage.*
import de.winkler.betoffice.storage.enums.*

def context = new ClassPathXmlApplicationContext(
    ['classpath:/betoffice-persistence.xml', 'classpath:/betoffice-datasource.xml', 'file:hibernate.xml'] as String[])
def maintenanceService = context.getBean('databaseMaintenanceService')
def master = context.getBean('masterDataManagerService');
def seasonService = context.getBean('seasonManagerService')


def wm2018 = new Season();
wm2018.name = 'WM'
wm2018.year = 2018
wm2018.mode = SeasonType.WC
wm2018.teamType = TeamType.FIFA

def wm2018_ = seasonService.createSeason(wm2018);
def seasonOptional = seasonService.findSeasonByName('WM Russland', '2018')
def season = seasonOptional.get()
print season.name + " - " + season.year

 // def bundesliga = master.findGroupType('1. Bundesliga');
def gruppeA = master.findGroupType('Gruppe A');
def gruppeB = master.findGroupType('Gruppe B');
def gruppeC = master.findGroupType('Gruppe C');
def gruppeD = master.findGroupType('Gruppe D');
def gruppeE = master.findGroupType('Gruppe E');
def gruppeF = master.findGroupType('Gruppe F');
def gruppeG = master.findGroupType('Gruppe G');
def gruppeH = master.findGroupType('Gruppe H');
def achtelfinale = master.findGroupType('Achtelfinale');
def viertelfinale = master.findGroupType('Viertelfinale');
def halbfinale = master.findGroupType('Halbfinale');
def finale = master.findGroupType('Finale');
def platz3 = master.findGroupType('Spiel um Platz 3');

def aegypten = master.findTeamByAlias('Ägypten');
def argentinien = master.findTeamByAlias('Argentinien');
def australien = master.findTeamByAlias('Australien');
def belgien = master.findTeamByAlias('Belgien');

def brasilien = master.findTeamByAlias('Brasilien');
def costaRica = master.findTeamByAlias('Costa Rica');
def daenemark = master.findTeamByAlias('Dänemark');
def uruguay = master.findTeamByAlias('Uruguay');

def deutschland = master.findTeamByAlias('Deutschland');
def england = master.findTeamByAlias('England');
def frankreich = master.findTeamByAlias('Frankreich');
def iran = master.findTeamByAlias('Iran');

def island = master.findTeamByAlias('Island');
def japan = master.findTeamByAlias('Japan');
def kolumbien = master.findTeamByAlias('Kolumbien');
def kroatien = master.findTeamByAlias('Kroatien');

def marokko = master.findTeamByAlias('Marokko');
def mexiko = master.findTeamByAlias('Mexiko');
def nigeria = master.findTeamByAlias('Nigeria');
def panama = master.findTeamByAlias('Panama');

def peru = master.findTeamByAlias('Peru');
def polen = master.findTeamByAlias('Polen');
def portugal = master.findTeamByAlias('Portugal');
def russland = master.findTeamByAlias('Russland');
 
def saudiArabien = master.findTeamByAlias('Saudi Arabien');
def schweden = master.findTeamByAlias('Schweden');
def schweiz = master.findTeamByAlias('Schweiz');
def senegal = master.findTeamByAlias('Senegal');

def serbien = master.findTeamByAlias('Serbien');
def spanien = master.findTeamByAlias('Spanien');
def suedkorea = master.findTeamByAlias('Südkorea');
def tunesien = master.findTeamByAlias('Tunesien');

def a = season.addGroupType(wm2018, gruppeA);
def b = season.addGroupType(wm2018, gruppeB);
def c = season.addGroupType(wm2018, gruppeC);
def d = season.addGroupType(wm2018, gruppeD);
def e = season.addGroupType(wm2018, gruppeE);
def f = season.addGroupType(wm2018, gruppeF);
def g = season.addGroupType(wm2018, gruppeG);
def h = season.addGroupType(wm2018, gruppeH);

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
