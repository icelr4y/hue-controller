package com.allenmp.hue;

public class LightState {

    private Integer lightNum;
    private Boolean isOn;
    private Integer brightness;
    private Integer temperature;

    public LightState() {
	super();
    }

    public LightState(Integer lightNum, Boolean isOn) {
	super();
	this.lightNum = lightNum;
	this.isOn = isOn;
    }

    public LightState(Integer lightNum, Boolean isOn, Integer brightness, Integer temperature) {
	super();
	this.lightNum = lightNum;
	this.isOn = isOn;
	this.brightness = brightness;
	this.temperature = temperature;
    }

    public Integer getLightNum() {
	return lightNum;
    }

    public void setLightNum(Integer lightNum) {
	this.lightNum = lightNum;
    }

    public Boolean isOn() {
	return isOn;
    }

    public void setOn(Boolean isOn) {
	this.isOn = isOn;
    }

    public Integer getBrightness() {
	return brightness;
    }

    public void setBrightness(Integer brightness) {
	this.brightness = brightness;
    }

    public Integer getTemperature() {
	return temperature;
    }

    public void setTemperature(Integer temperature) {
	this.temperature = temperature;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("{\"lightNum\":\"");
	builder.append(lightNum);
	builder.append("\", \"isOn\":\"");
	builder.append(isOn);
	builder.append("\", \"brightness\":\"");
	builder.append(brightness);
	builder.append("\", \"temperature\":\"");
	builder.append(temperature);
	builder.append("\"}");
	return builder.toString();
    }

}
