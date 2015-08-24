package com.jwdevs.ihome.openhab.message;

public class PirMessageImpl extends SensorMessageImpl<Boolean> implements PirMessage {
    public PirMessageImpl() {
        // TODO Auto-generated constructor stub
    }

    public PirMessageImpl(int number) {
        super(number, true);
    }
}
