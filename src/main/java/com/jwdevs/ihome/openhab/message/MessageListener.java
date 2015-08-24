package com.jwdevs.ihome.openhab.message;

/**
 * Communication interface implemented by the entities interested in receiving sensor messages;
 * 
 * @author jwozniak
 * 
 */
public interface MessageListener {

    public void onHeaterMessage(HeaterMessage msg);

    public void onThermometerMessage(ThermometerMessage msg);

    public void onPirMessage(PirMessage msg);

    public void onHumidityMessage(HumidityMessage msg);

    public void onThermostatDiagnosticsMessage(ThermostatDiagnosticsMessage msg);

}
