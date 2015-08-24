package com.jwdevs.ihome.openhab.message;

public interface RemoteMessage {
    //@formatter:off 
    /*
    #define TIME_MESSAGE 101 
    #define DAILY_RANGE_SETTING_MESSAGE 102 
    #define DAILY_DEFAULT_TEMP_MESSAGE 103 
    #define BOILER_ON_MESSAGE 104 
    #define BOILER_OFF_MESSAGE 105
    #define BOILER_ON_OFF_RESET_MESSAGE 106 
    #define BOILER_MODE_PRESENCE_MESSAGE 107 
    #define BOILER_MODE_CALENDAR_MESSAGE 108 
    #define BOILER_PRESENCE_TEMPS_MESSAGE 109
    #define REMOTE_PING_MESSAGE 110 
    #define STATE_MESSAGE 200
    */
   //@formatter:on

    public final static int HEATER_MESSAGE = 10;
    public final static int THERMOMETER_MESSAGE = 1;
    public final static int HUMIDITY_MESSAGE = 2;
    public final static int PIR_MESSAGE = 3;
    public final static int STATE_MESSAGE = 200;
    public final static int PING_MESSAGE = 110;
    public final static int TIME_MESSAGE = 101;

}
