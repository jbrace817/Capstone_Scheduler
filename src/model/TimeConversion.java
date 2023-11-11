package model;

import javafx.collections.ObservableList;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

public class TimeConversion {
    private static final ZoneId utc = ZoneId.of("Etc/UTC");
    private static final ZoneId systemZone = ZoneId.systemDefault();

    /** Converts current time to UTC. */
    public static LocalDateTime utcTime() {
        ZonedDateTime utcLdt = ZonedDateTime.now(utc);
        return utcLdt.toLocalDateTime();
    }

    /** Converts a timestamp in UTC to system time. */
    public static LocalDateTime utcTsToLocal(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(utc).withZoneSameInstant(systemZone).toLocalDateTime();
    }

    /** Converts system time to a UTC timestamp. */
    public static Timestamp localTimetoStamp(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt.atZone(systemZone).withZoneSameInstant(utc).toLocalDateTime());
    }

    /** Gets the local systems zone info. */
    public static ZoneId getSystemZone(){return systemZone;}

    /** Sets business hours and verifies that the start and end time are within the timeframe when saving an appointment. */
    public static Boolean businesshours(LocalDateTime start, LocalDateTime end, LocalDate appDate) {
        ZonedDateTime startZone = ZonedDateTime.of(start, systemZone);
        ZonedDateTime endZone = ZonedDateTime.of(end, systemZone);

        ZonedDateTime startTime = ZonedDateTime.of(appDate, LocalTime.of(8,0), getSystemZone());
        ZonedDateTime endTime = ZonedDateTime.of(appDate, LocalTime.of(22,0), getSystemZone());

        if (startZone.isBefore(startTime) || startZone.isAfter(endTime)||
        endZone.isBefore(startTime) || endZone.isAfter(endTime) || startZone.isAfter(endZone)) {
            return false;
        } else {
            return true;
        }

    }
}
