package com.jwdevs.ihome.openhab.message;

public class HumidityMessageImpl extends SensorMessageImpl<Float> implements
		HumidityMessage {

	public HumidityMessageImpl() {

	}

	public HumidityMessageImpl(int number, float humidity) {
		super(number, humidity);
	}

}
