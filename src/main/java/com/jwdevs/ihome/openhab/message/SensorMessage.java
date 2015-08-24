package com.jwdevs.ihome.openhab.message;

public interface SensorMessage<T> extends RemoteMessage {
	int getSensorNumber();

	T getValue();
}
