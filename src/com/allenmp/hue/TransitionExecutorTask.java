package com.allenmp.hue;

import java.util.TimerTask;

public class TransitionExecutorTask extends TimerTask {

    private Transition transition;
    private HueController controller; 
    
    public TransitionExecutorTask(Transition transition, HueController controller) {
	super();
	this.transition = transition;
	this.controller = controller;
    }

    @Override
    public void run() {
	for (LightState s : transition.getTargetStates()) {
	    controller.setState(s.getLightNum(), s, transition.getDuration());
	}
    }

}
