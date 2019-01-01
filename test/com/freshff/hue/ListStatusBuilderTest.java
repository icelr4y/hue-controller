package com.freshff.hue;

import static org.junit.Assert.*;

import org.glassfish.jersey.message.internal.StringBuilderUtils;
import org.json.JSONObject;
import org.junit.Test;

import com.freshff.common.StringUtil;

public class ListStatusBuilderTest {

    
    @Test
    public void testName() throws Exception {
	
    }
     
    @Test
    public void shouldParseLightsResponse() {
	String s = StringUtil.getResourceAsString(ListStatusBuilderTest.class, "lights-response.json");
	JSONObject json = new JSONObject(s);
	
	LightStatus status = LightsStatusBuilder.fromJson(1, json.get("1"));
	
	assertEquals(1, status.getLightNum());
	assertEquals("Floor Lamp", status.getLightName());
	assertEquals(254, status.getBrightness());
	assertEquals(230, status.getTemperature());
	
	
    }

}
