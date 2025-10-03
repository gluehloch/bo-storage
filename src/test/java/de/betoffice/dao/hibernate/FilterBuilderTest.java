package de.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.betoffice.storage.hibernate.FilterBuilder;

public class FilterBuilderTest {

    @Test
    void filterBuilder() {
        assertThat(FilterBuilder.filter("shortName", "c.shortName"))
                .isEqualTo(
                        " (:shortName IS NULL OR (:shortName IS NOT NULL AND LOWER(c.shortName) LIKE LOWER('%' || :shortName || '%'))) ");

        assertThat(FilterBuilder.filter("shortName", "c.reference.shortName"))
                .isEqualTo(
                        " (:shortName IS NULL OR (:shortName IS NOT NULL AND LOWER(c.reference.shortName) LIKE LOWER('%' || :shortName || '%'))) ");
    }

}
