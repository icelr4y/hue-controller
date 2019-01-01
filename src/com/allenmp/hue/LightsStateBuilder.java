package com.allenmp.hue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public final class LightsStateBuilder {

    
    public static List<LightState> parseList(String jsonStr) {
	JSONObject json = new JSONObject(jsonStr);
	List<LightState> result = new ArrayList<>();
	for (String key : json.keySet()) {
	    LightState s = parseOneState(Integer.valueOf(key), json.getJSONObject(key));
	    result.add(s);
	}
	return result;
    }
    
    public static LightState parseOneState(int lightNum, JSONObject json) {
	LightState s = new LightState();
	s.setLightNum(lightNum);
	s.setOn(json.getJSONObject("state").getBoolean("on"));
	s.setBrightness(json.getJSONObject("state").getInt("bri"));
	s.setTemperature(json.getJSONObject("state").getInt("ct"));
	return s;
    }
    
    public static JSONObject buildJsonBody(LightState state) { 
	JSONObject body = new JSONObject();
	if (state.isOn() != null) {
	    body.put("on", state.isOn());
	}
	if (state.getBrightness() != null) {
	    body.put("bri", state.getBrightness());
	}
	if (state.getTemperature() != null) {
	    body.put("ct", state.getTemperature());
	}
	return body;
    }

    public static JSONObject buildJsonBody(LightState newState, Duration transitionTime) {
	JSONObject body = buildJsonBody(newState);
	// Transition time is specified in multiples of 100ms
	body.put("transitiontime", transitionTime.getSeconds()*Constants.TRANSITION_INTERVALS_PER_SEC);
	return body;
    }
}
	