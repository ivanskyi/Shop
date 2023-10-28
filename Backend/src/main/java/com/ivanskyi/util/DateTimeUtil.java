package com.ivanskyi.util;

import java.time.Instant;

public class DateTimeUtil {

    public static long getCurrentEpochTime() {
        final Instant instant = Instant.now();
        return instant.getEpochSecond();
    }

    public static long calculateTimeDifferenceInMinutes(final long oldEpochTime, final long newEpochTime) {
        long timeDifferenceInSeconds = newEpochTime - oldEpochTime;
        return timeDifferenceInSeconds / 60;
    }
}
