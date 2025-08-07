/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.betoffice.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.betoffice.storage.GameList;
import de.betoffice.test.DateTimeDummyProducer;

/**
 * Look for the best date for a round. The round date is computed by all dates
 * of all matches of a round.
 *
 * @author by Andre Winkler
 */
public class BestDateFinderTest {

    @Test
    public void testBestRoundDateFinder2() {
        List<ZonedDateTime> dates = new ArrayList<>();
        dates.add(DateTimeDummyProducer.DT0_2014_08_22_203000);
        dates.add(DateTimeDummyProducer.DT1_2014_08_23_153000);
        dates.add(DateTimeDummyProducer.DT1_2014_08_23_153000);
        dates.add(DateTimeDummyProducer.DT3_2014_08_23_153000);
        dates.add(DateTimeDummyProducer.DT3_2014_08_23_153000);
        dates.add(DateTimeDummyProducer.DT5_2014_08_24_160000);
        dates.add(DateTimeDummyProducer.DT6_2014_08_24_190000);

        LocalDate bestDate = GameList.findBestDate(dates);
        assertThat(bestDate).isEqualTo(DateTimeDummyProducer.DT3_2014_08_23_153000.toLocalDate());
        
        dates.add(DateTimeDummyProducer.DT5_2014_08_24_160000);
        dates.add(DateTimeDummyProducer.DT5_2014_08_24_160000);
        dates.add(DateTimeDummyProducer.DT6_2014_08_24_190000);
        dates.add(DateTimeDummyProducer.DT6_2014_08_24_190000);
        
        LocalDate bestDate2 = GameList.findBestDate(dates);
        assertThat(bestDate2).isEqualTo(DateTimeDummyProducer.DT5_2014_08_24_160000.toLocalDate());
    }

}
