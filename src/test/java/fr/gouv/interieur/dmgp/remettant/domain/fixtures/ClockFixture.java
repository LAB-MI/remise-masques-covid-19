package fr.gouv.interieur.dmgp.remettant.domain.fixtures;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockFixture {
    private static final LocalDateTime NOW = LocalDateTime.of(2019, 6, 12, 13, 39, 45);
    private static final String ZONE_UTC = "UTC";

    public static Clock clock() {
        return Clock.fixed(NOW.atZone(ZoneId.of(ZONE_UTC)).toInstant(), ZoneId.of(ZONE_UTC));
    }

    public static LocalDateTime now() {
        return NOW;
    }
}
