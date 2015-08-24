package com.jwdevs.ihome.openhab.message;

public class HeaterDiagnosticsMessageImpl implements ThermostatDiagnosticsMessage {

    private boolean on;
    private boolean forced;
    private boolean remoteTimeout;
    private boolean tempTimeout;
    private boolean presenceMode;
    private float refTemp;
    private float lastLocalTemp;
    private float meanLocalTemp;
    private float lastRemoteTemp;
    private float meanRemoteTemp;
    private float lastLocalHumidity;
    private boolean localMovementDetectorState;
    private boolean presenceDetected;
    private long time;

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public void setRemoteTimeout(boolean remoteTimeout) {
        this.remoteTimeout = remoteTimeout;
    }

    public void setTempTimeout(boolean tempTimeout) {
        this.tempTimeout = tempTimeout;
    }

    public void setPresenceMode(boolean presenceMode) {
        this.presenceMode = presenceMode;
    }

    public void setRefTemp(float refTemp) {
        this.refTemp = refTemp;
    }

    public void setLastLocalTemp(float lastLocalTemp) {
        this.lastLocalTemp = lastLocalTemp;
    }

    public void setMeanLocalTemp(float meanLocalTemp) {
        this.meanLocalTemp = meanLocalTemp;
    }

    public void setLastRemoteTemp(float lastRemoteTemp) {
        this.lastRemoteTemp = lastRemoteTemp;
    }

    public void setMeanRemoteTemp(float meanRemoteTemp) {
        this.meanRemoteTemp = meanRemoteTemp;
    }

    public void setLastLocalHumidity(float lastLocalHumidity) {
        this.lastLocalHumidity = lastLocalHumidity;
    }

    public void setLocalMovementDetectorState(boolean localMovementDetectorState) {
        this.localMovementDetectorState = localMovementDetectorState;
    }

    public void setPresenceDetected(boolean presenceDetected) {
        this.presenceDetected = presenceDetected;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean isOn() {
        return this.on;
    }

    @Override
    public boolean isForced() {
        // TODO Auto-generated method stub
        return forced;
    }

    @Override
    public boolean isRemoteTimeout() {
        // TODO Auto-generated method stub
        return remoteTimeout;
    }

    @Override
    public boolean isTempTimeout() {
        // TODO Auto-generated method stub
        return tempTimeout;
    }

    @Override
    public boolean isPresenceMode() {
        // TODO Auto-generated method stub
        return presenceMode;
    }

    @Override
    public float getRefTemp() {
        // TODO Auto-generated method stub
        return refTemp;
    }

    @Override
    public float getLastLocalTemp() {
        // TODO Auto-generated method stub
        return lastLocalTemp;
    }

    @Override
    public float getMeanLocalTemp() {
        // TODO Auto-generated method stub
        return meanLocalTemp;
    }

    @Override
    public float getLastRemoteTemp() {
        // TODO Auto-generated method stub
        return lastRemoteTemp;
    }

    @Override
    public float getMeanRemoteTemp() {
        // TODO Auto-generated method stub
        return meanRemoteTemp;
    }

    @Override
    public float getLastLocalHumidity() {
        // TODO Auto-generated method stub
        return lastLocalHumidity;
    }

    @Override
    public boolean getLocalMovementDetectorState() {
        // TODO Auto-generated method stub
        return localMovementDetectorState;
    }

    @Override
    public boolean isPresenceDetected() {
        // TODO Auto-generated method stub
        return presenceDetected;
    }

    @Override
    public long getTime() {
        // TODO Auto-generated method stub
        return time;
    }

}
