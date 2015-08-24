package com.jwdevs.ihome.openhab.message;

public class HeaterMessageImpl extends SensorMessageImpl<Boolean> implements
		HeaterMessage {

	/**
	 * A constructor that helps making a polymorphic configuration
	 */
	public HeaterMessageImpl() {
		super();
	}

	public HeaterMessageImpl(int number, boolean on) {
		super(number, on);
	}

}
