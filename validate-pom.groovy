println """

POM Checker is starting ...

"""
def pomAsFile = new File("pom.xml")
def pom = new XmlSlurper().parse(pomAsFile)

def derivedGroupId = pom.groupId?.text() ?: pom.parent.groupId

println "Parent POM:"
println "    ${pom.parent.groupId}:${pom.parent.artifactId}:${pom.parent.version}"
println "Project POM:"
println "    ${derivedGroupId}:${pom.artifactId}:${pom.version}"
