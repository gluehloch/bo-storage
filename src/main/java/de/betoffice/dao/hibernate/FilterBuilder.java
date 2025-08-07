package de.betoffice.dao.hibernate;

public class FilterBuilder {

    private static final String WHERE_CONDITION = " (:%1$s IS NULL OR (:%1$s IS NOT NULL AND LOWER(%2$s) LIKE LOWER('%%' || :%1$s || '%%'))) ";
    
    public static String filter(String parameterName, String entityPath) {
        return String.format(WHERE_CONDITION, parameterName, entityPath); 
    }

}
