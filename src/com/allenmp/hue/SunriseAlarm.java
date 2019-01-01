package com.allenmp.hue;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SunriseAlarm {
    private static final Logger LOG = LoggerFactory.getLogger(SunriseAlarm.class);
    private Duration length;
    private Instant time;

    public SunriseAlarm(Duration length, Instant time) {
	super();
	this.length = length;
	this.time = time;
    }

    public void schedule() {
	Date startTime = Date.from(time.minus(length));

	Timer timer = new Timer("SunriseAlarm");
	timer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		Routine sunrise = RoutineGenerator.sunrise(length);
		RoutineExecutor exec = new RoutineExecutor(sunrise);
		exec.execute();
	    }
	}, startTime);
	LOG.info("Scheduled alarm to start at {}", startTime);
    }

}
