package de.winkler.betoffice.dao.hibernate;

public class FilterBuilder {

    public static String filter(String prefix, String filter) {
        return String.format(" (:%1$s IS NULL OR (:%1$s IS NOT NULL AND LOWER(%2$s.%1$s) LIKE LOWER('%%' || :shortName || '%%'))) ", filter, prefix); 
    }

}
