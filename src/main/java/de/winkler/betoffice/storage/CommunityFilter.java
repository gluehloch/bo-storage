package de.winkler.betoffice.storage;

public class CommunityFilter {

    private String name;
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "CommunityFilter [name=" + name + ", shortName=" + shortName + "]";
    }

    public static CommunityFilter shortName(String shortName) {
        CommunityFilter cf = new CommunityFilter();
        cf.setShortName(shortName);
        return cf;
    }
    
    public static CommunityFilter name(String name) {
        CommunityFilter cf = new CommunityFilter();
        cf.setName(name);
        return cf;
    }

}
