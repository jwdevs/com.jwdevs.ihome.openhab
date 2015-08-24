package com.jwdevs.ihome.openhab.message;

public interface ThermostatDiagnosticsMessage extends RemoteMessage {
    boolean isOn();

    boolean isForced();

    boolean isRemoteTimeout();

    boolean isTempTimeout();

    boolean isPresenceMode();

    float getRefTemp();

    float getLastLocalTemp();

    float getMeanLocalTemp();

    float getLastRemoteTemp();

    float getMeanRemoteTemp();

    float getLastLocalHumidity();

    boolean getLocalMovementDetectorState();

    boolean isPresenceDetected();

    long getTime();

}
