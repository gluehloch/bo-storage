def red() {
    print "\033[31;1m"
}

def yellow() {
    print "\033[33m"
}

def green() {
    print "\033[32m"
}

def reset() {
    print "\033[0m"
}

green()
println """

///  POM Checker is starting ...

"""
reset()

def fg = 30
def bg = 46
def style = "${(char)27}[$fg;$bg"+"m"

def pomAsFile = new File("pom.xml")
def pom = new XmlSlurper().parse(pomAsFile)

def derivedGroupId = pom.groupId?.text() ?: pom.parent.groupId

def enableWarning = false

println "Parent POM:"
println "    ${pom.parent.groupId}:${pom.parent.artifactId}:${pom.parent.version}"
println "Project POM:"
println "    ${derivedGroupId}:${pom.artifactId}:${pom.version}"
reset()

println "Dependencies:"
pom.dependencies.dependency.each { dependency ->
    if (dependency.version.toString().contains('SNAPSHOT')) {
        yellow()
        enableWarning = true
        println "    WARNING: SNAPSHOT dependency found!"
        println "        ${dependency.groupId}:${dependency.artifactId}:${dependency.version}"
        reset()
    } else {
        println "    ${dependency.groupId}:${dependency.artifactId}:${dependency.version}"
    }
}

def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'git status'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "out> $sout err> $serr"

if (enableWarning) {
    red()
    println """
///  Check you dependencies!
"""
} else {
    reset()
    println "Release me!"
}

// Check: changes.xml No ??-?? dates declated?
// Check: Expected version defined? Take version as parameter.
