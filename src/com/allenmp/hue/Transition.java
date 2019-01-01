package com.allenmp.hue;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Transition {

    private Duration startTime = Duration.ZERO;
    private Duration duration = Constants.DEFAULT_TRANSITION_TIME;
    private Set<LightState> states = new HashSet<>();

    public Duration getStartTime() {
	return startTime;
    }

    public void setStartTime(Duration duration) {
	this.startTime = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration transitionTime) {
	if (transitionTime.compareTo(Constants.MAX_TRANSITION_TIME) > 0) {
	    throw new IllegalArgumentException("Transition Time may not be greater than " + Constants.MAX_TRANSITION_TIME.toString());
	}
        this.duration = transitionTime;
    }

    public Set<LightState> getTargetStates() {
	return states;
    }

    public void setTargetStates(Set<LightState> states) {
	this.states = states;
    }
    
    /**
     * Make sure to only set state for each light once
     * @param state
     */
    public void setTargetState(LightState state) {
	this.states.add(state);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("{\"startTime\":\"");
	builder.append(startTime);
	builder.append("\", \"duration\":\"");
	builder.append(duration);
	builder.append("\", \"states\":\"");
	builder.append(states);
	builder.append("\"}");
	return builder.toString();
    }

}
