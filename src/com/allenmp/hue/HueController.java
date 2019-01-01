package com.allenmp.hue;

import java.time.Duration;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HueController {
    
    private static final Logger LOG = LoggerFactory.getLogger(HueController.class);
    
    // To get a new IP:
    // https://www.meethue.com/api/nupnp
    // To check APP_ID: http://[IP]/api/[APP_ID]
    private static final String BRIDGE_IP = "http://192.168.1.7";
    private static final String APP_ID = "9ASisQlxiWGCxuc5ioSIbshsEp1PDPSOMiunrPRN";

    private final Client client;

    public HueController() {
	this.client = ClientBuilder.newClient();
    }

    public List<LightState> getLightsState() {

	WebTarget target = client.target(BRIDGE_IP).path("api").path(APP_ID).path("lights");
	LOG.info("URL: {}", target.getUri().getPath());
	String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

	List<LightState> status = LightsStateBuilder.parseList(response);
	return status;
    }

    public void allOn() {
	setOn(1, true);
	setOn(2, true);
    }
    
    public void turnOn(int light) {
	setOn(light, true);
    }
    
    public void allOff() {
	setOn(1, false);
	setOn(2, false);
	
    }
    
    public void turnOff(int light) {
	setOn(light, false);
    }


    public void setOn(int light, boolean isOn) {
	LightState newState = new LightState();
	newState.setOn(isOn);
	setState(light, newState);
    }

    public void setBrightness(int light, int brightness) {
	LightState newState = new LightState();
	newState.setBrightness(brightness);
	setState(light, newState);
    }
    
    public void setTemperature(int light, int temperature) {
	LightState newState = new LightState();
	newState.setTemperature(temperature);
	setState(light, newState);
    }
    
    public void setState(int light, LightState newStatus) {
	setState(light, newStatus, Constants.DEFAULT_TRANSITION_TIME);
    }

    public void setState(int light, LightState newState, Duration transitionTime) {
	WebTarget target = client.target(BRIDGE_IP).path("api").path(APP_ID).path("lights").path(String.valueOf(light)).path("state");
	JSONObject body = LightsStateBuilder.buildJsonBody(newState, transitionTime);
	
	LOG.info("API Call: URL={}, Body={}", target.getUri().getPath(), body);
	try {
	    Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(body.toString(), MediaType.APPLICATION_JSON));
	    int status = response.getStatus();
	    LOG.info("Response ({}): {}", status, response.readEntity(String.class));
	} catch (ResponseProcessingException e1) {
	    LOG.error("ReponseProcessingException: {}", e1.getMessage());
	} catch (ProcessingException e2) {    
	    LOG.error("ProcessingException: {}", e2.getMessage());
	}
    }
    
}
