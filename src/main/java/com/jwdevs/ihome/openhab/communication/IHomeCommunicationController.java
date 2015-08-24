package com.jwdevs.ihome.openhab.communication;

import com.jwdevs.ihome.openhab.message.MessageListener;

/**
 * Handles remote communication with the house devices using the serial protocol.
 * 
 * @author jwozniak
 * 
 */
public interface IHomeCommunicationController {
    void registerListener(MessageListener listener);

    public abstract void setOff();

    public abstract void setOn();

    public abstract void setTime();

    public void setPresenceMode();

    public void setCalendarMode();

    public void setCalendarDailyTemperatureRange(int weekDay, int rangeIndex, int startHour, int startMinute, int stopHour, int stopMinute);

    public void setCalendarDefaultDailyTemp(int weekDay, float defaultTemp);

    public void setPresenceDetected();

    public abstract void setPresenceTemperatures(float comfortTemp, float defaultTemp, float nightTemp);

    public void serverHeartBeat();

}
