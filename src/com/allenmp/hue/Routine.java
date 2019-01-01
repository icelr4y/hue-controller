package com.allenmp.hue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Routine {

    private String name = "DefaultRoutineName";
    private Duration period = Duration.ZERO;
    private Set<Transition> transitions = new HashSet<>();

    public Routine() {
	super();
    }

    public Routine(String name) {
	super();
	this.name = name;
    }

    public Routine(String name, Set<Transition> transitions) {
	super();
	this.name = name;
	this.transitions.addAll(transitions);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Duration getPeriod() {
	return period;
    }

    public void setPeriod(Duration period) {
	this.period = period;
    }

    /**
     * Make this routine repeat when finished
     */
    public void setRepeating(boolean isRepeating) {
	if (isRepeating) {
	    setPeriod(getTotalDuration());
	} else {
	    setPeriod(Duration.ZERO);
	}
    }
    
    public void addTransition(Transition wpt) {
	transitions.add(wpt);
    }

    public void clear() {
	transitions.clear();
    }

    /**
     * Add another routine to this one, starting at the provided time. All times
     * in the second Routine are shifted by the required amount
     * 
     * @param other
     * @param startingAt
     */
    public void add(Routine other, Duration startingAt) {
	for (Transition w : other.getTransitions()) {
	    w.setStartTime(w.getStartTime().plus(startingAt));
	    addTransition(w);
	}
    }

    /**
     * Add another routine to the end of this one, starting when the last transition finishes
     * @param other
     */
    public void addNext(Routine other) {
	Duration totalLength = getTotalDuration();
	add(other, totalLength);
    }
    
    public Duration getTotalDuration() {
	List<Transition> wpts = getTransitions();
	if (wpts.isEmpty()) { 
	    return Duration.ZERO;
	}
	Transition last = wpts.get(wpts.size()-1);
	return last.getStartTime().plus(last.getDuration());
    }

    public List<Transition> getTransitions() {
	List<Transition> wpts = new ArrayList<>(transitions);
	Collections.sort(wpts, new Comparator<Transition>() {
	    @Override
	    public int compare(Transition o1, Transition o2) {
		Duration d1 = o1.getStartTime();
		Duration d2 = o2.getStartTime();
		return d1.compareTo(d2);
	    }
	});
	return wpts;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("{\"name\":\"");
	builder.append(name);
	builder.append("\", \"period\":\"");
	builder.append(period);
	builder.append("\", \"transitions\":\"");
	builder.append(getTransitions());
	builder.append("\"}");
	return builder.toString();
    }

}
