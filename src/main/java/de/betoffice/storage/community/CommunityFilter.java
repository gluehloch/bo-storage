package de.betoffice.storage.community;

public class CommunityFilter {

    private String name;
    private String shortName;
    private String year;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "CommunityFilter [name=" + name + ", shortName=" + shortName + ", year=" + year + "]";
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

    public static CommunityFilter year(String year) {
        CommunityFilter cf = new CommunityFilter();
        cf.setShortName(year);
        return cf;
    }

}
