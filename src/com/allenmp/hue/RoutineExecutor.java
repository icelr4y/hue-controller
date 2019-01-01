package com.allenmp.hue;

import java.time.Duration;
import java.util.Objects;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jersey.repackaged.com.google.common.collect.Sets;

public class RoutineExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(RoutineExecutor.class);
    
    private Timer timer;
    private Routine routine;
    
    public RoutineExecutor(Routine routine) {
	Objects.requireNonNull(routine);
	this.routine = routine;
	this.timer = new Timer(routine.getName());
    }
    
    public RoutineExecutor(Transition transition) {
	Objects.requireNonNull(transition);
	this.routine = new Routine("CustomRoutine", Sets.newHashSet(transition));
	this.timer = new Timer(routine.getName());
    }

    public void execute() {
	HueController controller = new HueController();
	Duration period = routine.getPeriod();
	boolean isRepeating = period != Duration.ZERO;

	for (Transition tr : routine.getTransitions()) {
	    TransitionExecutorTask task = new TransitionExecutorTask(tr, controller);
	    long interval = tr.getStartTime().getSeconds()*1000;
	    if (isRepeating) {
		timer.scheduleAtFixedRate(task, interval, period.getSeconds()*1000);
		LOG.debug("Added to schedule (repeating {} sec): {}", period.getSeconds(), tr);
	    } else {
		timer.schedule(task, interval);
		LOG.debug("Added to schedule: {}", tr);
	    }
	}
    }

}
