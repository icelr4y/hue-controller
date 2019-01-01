package com.allenmp.hue;

import java.time.Duration;

/**
 * @see <a href=
 *      "https://developers.meethue.com/documentation/lights-api">Developer API
 *      Reference</a>
 * @author mallen
 *
 */
public class Constants {
    public static final int MIN_BRIGHTNESS = 1;
    public static final int MAX_BRIGHTNESS = 254;
    public static final int MIN_TEMP = 153;
    public static final int MAX_TEMP = 454;
    public static final Duration DEFAULT_TRANSITION_TIME = Duration.ofMillis(400);
    public static final long TRANSITION_INTERVALS_PER_SEC = 10L;
    public static final Duration MAX_TRANSITION_TIME = Duration.ofSeconds(65535/TRANSITION_INTERVALS_PER_SEC);

    // Some helpful shortcuts
    public static final int RED = MAX_TEMP;
    public static final int BLUE = MIN_TEMP;
    public static final int WHITE = (RED + BLUE) / 2;
    
}
