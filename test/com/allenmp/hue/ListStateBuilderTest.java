package com.allenmp.hue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ListStateBuilderTest {

    @Test
    public void shouldParseLightsResponse() {

	URL url = Resources.getResource(ListStateBuilderTest.class, "lights-response.json");
	String s = null;
	try {
	    s = Resources.toString(url, Charsets.UTF_8);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    fail();
	}
	JSONObject json = new JSONObject(s);

	LightState status = LightsStateBuilder.parseOneState(1, (JSONObject) json.get("1"));

	assertTrue(status.isOn());
	assertEquals(1, status.getLightNum().intValue());
	assertEquals(254, status.getBrightness().intValue());
	assertEquals(230, status.getTemperature().intValue());

    }

}
