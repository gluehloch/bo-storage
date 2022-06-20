package de.winkler.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class FilterBuilderTest {

    @Test
    void filterBuilder() {
        assertThat(FilterBuilder.filter("c","shortName")).isEqualTo(" (:shortName IS NULL OR (:shortName IS NOT NULL AND LOWER(c.shortName) LIKE LOWER('%' || :shortName || '%'))) ");
    }
    
}
