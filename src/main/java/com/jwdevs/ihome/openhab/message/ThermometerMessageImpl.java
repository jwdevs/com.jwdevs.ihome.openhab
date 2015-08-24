package com.jwdevs.ihome.openhab.message;

public class ThermometerMessageImpl extends SensorMessageImpl<Float> implements
		ThermometerMessage {

	public ThermometerMessageImpl() {
		// TODO Auto-generated constructor stub
	}

	public ThermometerMessageImpl(int nm, float temp) {
		super(nm, temp);
	}

}
