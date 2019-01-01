package com.allenmp.hue;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

public class RoutineGenerator {

    /**
     * Turn on all lights at max brightness and temp
     * 
     * @param duration
     *            Duration of the transition
     * @return
     */
    public static Routine allMaxOn(Duration duration) {

	Transition on = new Transition();
	on.setDuration(duration);
	on.setTargetState(new LightState(1, true, Constants.MAX_BRIGHTNESS, Constants.MAX_TEMP));
	on.setTargetState(new LightState(2, true, Constants.MAX_BRIGHTNESS, Constants.MAX_TEMP));

	return new Routine("All On", new HashSet<>(Arrays.asList(on)));
    }

    /**
     * Turn all lights off, leaving temperature alone
     * 
     * @param duration
     *            Duration of the transition
     * @return
     */
    public static Routine allOff(Duration duration) {

	Transition off = new Transition();
	off.setDuration(duration);
	off.setTargetState(new LightState(1, false));
	off.setTargetState(new LightState(2, false));

	return new Routine("All Off", new HashSet<>(Arrays.asList(off)));
    }

    public static Routine dimAll(Duration duration) {
	// Use TransitionTime to dim the lights!
	Transition dim = new Transition();
	dim.setDuration(duration);

	// start at whatever the current temp is (but make sure it's on);
	dim.setTargetState(new LightState(1, true, Constants.MIN_BRIGHTNESS, null));
	dim.setTargetState(new LightState(2, true, Constants.MIN_BRIGHTNESS, null));

	// turn off at the end
	Transition off = new Transition();
	off.setStartTime(duration);
	off.setDuration(Duration.ZERO);
	off.setTargetState(new LightState(1, false));
	off.setTargetState(new LightState(2, false));

	return new Routine("Dim All", new HashSet<>(Arrays.asList(dim, off)));
    }

    public static Routine sunrise(Duration duration) {
	// start at dimmed and red
	Transition w1 = new Transition();
	w1.setStartTime(Duration.ZERO);
	w1.setDuration(Duration.ofSeconds(10));
	w1.setTargetState(new LightState(1, true, Constants.MIN_BRIGHTNESS, Constants.RED));
	w1.setTargetState(new LightState(2, true, Constants.MIN_BRIGHTNESS, Constants.RED));

	// finish at bright blue
	Transition w2 = new Transition();
	w2.setStartTime(Duration.ofSeconds(1));
	w2.setDuration(duration);
	w2.setTargetState(new LightState(1, true, Constants.MAX_BRIGHTNESS, Constants.BLUE));
	w2.setTargetState(new LightState(2, true, Constants.MAX_BRIGHTNESS, Constants.BLUE));

	Routine alarm = new Routine("Sunrise", new HashSet<>(Arrays.asList(w1, w2)));
	alarm.setPeriod(Duration.ofHours(24));
	
	return alarm;

    }

}
