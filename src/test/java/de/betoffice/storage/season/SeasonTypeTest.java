/*
 * $Id: SeasonTypeTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2019 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.betoffice.storage.season;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test of class {@link SeasonType}.
 *
 * @author by Andre Winkler
 */
public class SeasonTypeTest {

    @Test
    public void testSeasonTypeOrdinal() {
        assertThat(SeasonType.LEAGUE.ordinal()).isEqualTo(0);
        assertThat(SeasonType.CUP.ordinal()).isEqualTo(1);
        assertThat(SeasonType.UEFACUP.ordinal()).isEqualTo(2);
        assertThat(SeasonType.CL.ordinal()).isEqualTo(3);
        assertThat(SeasonType.WC.ordinal()).isEqualTo(4);
        assertThat(SeasonType.EC.ordinal()).isEqualTo(5);
    }

}
