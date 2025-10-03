package de.betoffice.test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeDummyProducer {

    public static final ZoneId EUROPE_BERLIN = ZoneId.of("Europe/Berlin");

    public static final LocalDateTime LDT_2002_01_01 = LocalDateTime.of(2002, 1, 1, 0, 0);
    public static final LocalDateTime LDT_2002_01_02 = LocalDateTime.of(2002, 1, 2, 0, 0);
    public static final LocalDateTime LDT_1971_03_24 = LocalDateTime.of(1971, 3, 24, 0, 0);
    public static final LocalDateTime LDT_1971_03_24__20_00 = LocalDateTime.of(1971, 3, 24, 20, 0);

    public static final LocalDateTime LDT_2014_08_22__20_30 = LocalDateTime.of(2014, 8, 22, 20, 30, 0);
    public static final LocalDateTime LDT_2014_08_23__15_30 = LocalDateTime.of(2014, 8, 23, 15, 30, 0);
    public static final LocalDateTime LDT_2014_08_24__16_00 = LocalDateTime.of(2014, 8, 23, 16, 00, 0);
    public static final LocalDateTime LDT_2014_08_24__19_00 = LocalDateTime.of(2014, 8, 23, 19, 00, 0);

    public static final ZonedDateTime DATE_2002_01_01 = ZonedDateTime.of(LDT_2002_01_01, EUROPE_BERLIN);
    public static final ZonedDateTime DATE_2002_01_02 = ZonedDateTime.of(LDT_2002_01_02, EUROPE_BERLIN);
    
    public static final ZonedDateTime DATE_1971_03_24 = ZonedDateTime.of(LDT_1971_03_24, EUROPE_BERLIN);
    public static final ZonedDateTime DATE_1971_03_24__20_00 = ZonedDateTime.of(LDT_1971_03_24__20_00, EUROPE_BERLIN);
    
    // ----------------------------------------------------------------------------------------------------------------

    public static final ZonedDateTime DT0_2014_08_22_203000 = ZonedDateTime.of(LDT_2014_08_22__20_30, EUROPE_BERLIN);

    public static final ZonedDateTime DT1_2014_08_23_153000 = ZonedDateTime.of(LDT_2014_08_23__15_30, EUROPE_BERLIN);
    public static final ZonedDateTime DT2_2014_08_23_153000 = ZonedDateTime.of(LDT_2014_08_23__15_30, EUROPE_BERLIN);
    public static final ZonedDateTime DT3_2014_08_23_153000 = ZonedDateTime.of(LDT_2014_08_23__15_30, EUROPE_BERLIN);
    public static final ZonedDateTime DT4_2014_08_23_153000 = ZonedDateTime.of(LDT_2014_08_23__15_30, EUROPE_BERLIN);

    public static final ZonedDateTime DT5_2014_08_24_160000 = ZonedDateTime.of(LDT_2014_08_24__16_00, EUROPE_BERLIN);
    public static final ZonedDateTime DT6_2014_08_24_190000 = ZonedDateTime.of(LDT_2014_08_24__19_00, EUROPE_BERLIN);
    
}
