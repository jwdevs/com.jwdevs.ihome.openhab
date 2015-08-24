package com.jwdevs.ihome.openhab.message;

public abstract class SensorMessageImpl<T> implements SensorMessage<T> {
	private int sensorNumber;
	private T value;

	public SensorMessageImpl() {
	}

	public SensorMessageImpl(int sensor, T value) {
		this.sensorNumber = sensor;
		this.value = value;
	}

	@Override
	public int getSensorNumber() {
		return this.sensorNumber;
	}

	@Override
	public T getValue() {
		return value;
	}
}
