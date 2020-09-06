package dev.codethat.moneyplant.core.util;

import javax.inject.Named;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Named
public class DateUtil {
    public Instant getNextRun(final String startTime, final ZoneId zoneId) {
        String[] timeArray = startTime.split("-");
        return ZonedDateTime.now(zoneId)
                .withHour(Integer.parseInt(timeArray[0]))
                .withMinute(Integer.parseInt(timeArray[1])).withSecond(0).toInstant();
    }
}
