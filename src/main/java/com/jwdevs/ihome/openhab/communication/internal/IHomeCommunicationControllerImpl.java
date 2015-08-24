package com.jwdevs.ihome.openhab.communication.internal;

import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Scanner;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwdevs.ihome.openhab.communication.IHomeCommunicationController;
import com.jwdevs.ihome.openhab.message.HeaterDiagnosticsMessageImpl;
import com.jwdevs.ihome.openhab.message.HeaterMessageImpl;
import com.jwdevs.ihome.openhab.message.HumidityMessageImpl;
import com.jwdevs.ihome.openhab.message.MessageListener;
import com.jwdevs.ihome.openhab.message.PirMessageImpl;
import com.jwdevs.ihome.openhab.message.RemoteMessage;
import com.jwdevs.ihome.openhab.message.ThermometerMessageImpl;

/**
 * This class allows to send remote messages to the thermostat using a simple {x,y,z} serial protocol with XBee.
 * 
 * 
 * 
 * 
 * @author jwozniak
 * 
 */
public class IHomeCommunicationControllerImpl implements IHomeCommunicationController, ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(IHomeCommunicationControllerImpl.class);

    private SerialPort serialPort;
    private StringBuilder strBuilder = new StringBuilder();
    private String port;
    private boolean initialized = false;

    private MessageListener listener;

    private void writeSerial(String mesg) {
        try {
            if (serialPort.isOpened()) {
                serialPort.writeBytes(mesg.getBytes());
            } else {
                logger.error("Serial port is closed");
            }
        } catch (SerialPortException ex) {
            logger.error("Serial write exception", ex);
        }
    }

    /*
     * Package protected for tests.
     */
    synchronized void processData(String data) {
        logger.debug("Appending data: {}", data);
        strBuilder.append(data);
        logger.debug("Total data: {}", data);
        removeUntilStartMessage();
        int start = -1;
        int stop = -1;
        while (true) {
            start = strBuilder.indexOf("{");
            if (start != -1) {
                // find corresponding end
                stop = strBuilder.indexOf("}", start + 1);
                int checkBrokenStart = strBuilder.indexOf("{", start + 1);
                if (stop != -1 && checkBrokenStart != -1 && checkBrokenStart < stop) {
                    // the message is scrambled of the form "{12;{12;23}
                    strBuilder = new StringBuilder(strBuilder.substring(checkBrokenStart));
                    continue;
                } else if (stop != -1) {
                    String mesg = strBuilder.substring(start + 1, stop);
                    processMessage(mesg);
                    int newStart = stop + 1;
                    if (newStart >= strBuilder.length()) {
                        strBuilder = new StringBuilder();
                    } else {
                        strBuilder = new StringBuilder(strBuilder.substring(newStart));
                    }
                } else {
                    break;
                }

            } else {
                break;
            }
        }

    }

    /*
     * For the scrambled messages like '12;34}{10;23' it removes the trailing characters until the first '{' character is found.
     */
    private void removeUntilStartMessage() {
        int start = strBuilder.indexOf("{");
        if (start > 0) {
            strBuilder = new StringBuilder(strBuilder.substring(start));
        }
    }

    private void processMessage(String mesg) {
        logger.debug("Message received: {}", mesg);
        if (listener == null) {
            logger.warn("Message listener not set in {} , aborting processing this message: {}", this.hashCode(), mesg);
            return;
        }

        try (Scanner scanner = new Scanner(mesg)) {
            scanner.useDelimiter(";");

            switch (scanner.nextInt()) {
            case RemoteMessage.THERMOMETER_MESSAGE:
                logger.debug("ThermomenterMessage received");
                listener.onThermometerMessage(new ThermometerMessageImpl(scanner.nextInt(), scanner.nextFloat()));
                break;
            case RemoteMessage.HUMIDITY_MESSAGE:
                logger.debug("HumidityMessage received");
                listener.onHumidityMessage(new HumidityMessageImpl(scanner.nextInt(), scanner.nextFloat()));
                break;
            case RemoteMessage.PIR_MESSAGE:
                logger.debug("PirMessage received");
                listener.onPirMessage(new PirMessageImpl(scanner.nextInt()));
                break;
            case RemoteMessage.HEATER_MESSAGE:
                logger.debug("HeaterMessage received");
                listener.onHeaterMessage(new HeaterMessageImpl(scanner.nextInt(), scanner.nextInt() > 0 ? true : false));
                break;
            case RemoteMessage.STATE_MESSAGE:
                logger.debug("HeaterMessage received");
                HeaterDiagnosticsMessageImpl msg = new HeaterDiagnosticsMessageImpl();
                msg.setOn(scanner.nextInt() == 1 ? true : false);
                msg.setForced(scanner.nextInt() == 1 ? true : false);
                msg.setPresenceMode(scanner.nextInt() == 1 ? true : false);
                msg.setPresenceDetected(scanner.nextInt() == 1 ? true : false);
                msg.setRemoteTimeout(scanner.nextInt() == 1 ? true : false);
                msg.setTempTimeout(scanner.nextInt() == 1 ? true : false);
                msg.setRefTemp(scanner.nextFloat());
                msg.setLastRemoteTemp(scanner.nextFloat());
                msg.setMeanRemoteTemp(scanner.nextFloat());
                msg.setLastLocalTemp(scanner.nextFloat());
                msg.setMeanLocalTemp(scanner.nextFloat());
                msg.setTime(scanner.nextLong());
                listener.onThermostatDiagnosticsMessage(msg);
                break;
            default:
                logger.error("Unsupported message {}", mesg);
            }

        } catch (RuntimeException ex) {
            logger.error("Exception processing message {}", mesg, ex);
        }
    }

    public void activate() {
        logger.debug("Activated: " + this.hashCode());
    }

    public void modified() {
        logger.debug("Modified: " + this.hashCode());
    }

    public void deactivate() {

        if (this.serialPort != null && this.serialPort.isOpened()) {
            try {
                this.serialPort.removeEventListener();
                this.serialPort.closePort();
            } catch (SerialPortException e) {
                logger.error("Error closing serial port", e);
            }
            this.serialPort = null;
        }
        logger.debug("Deactivated: " + this.hashCode());
    }

    /**
     * 
     * 
     * @param port
     */
    public IHomeCommunicationControllerImpl() {
        logger.debug("IHomeCommunicationControllerImpl created: " + this.hashCode());
    }

    @Override
    public void setTime() {
        /*
         * OffsetDateTime timestamp = OffsetDateTime.now(); System.out.println(timestamp); System.out.println(time);
         * 
         * String out = "{2;" + (timestamp.getLong(ChronoField.INSTANT_SECONDS)+timestamp .getLong(ChronoField.OFFSET_SECONDS)) + "}";
         * System.out.println(out);
         */

        long time = new Date().getTime() / 1000;

        writeSerial("{101;" + Long.toString(time) + "}");
    }

    @Override
    public void setOn() {
        writeSerial("{104}");

    }

    @Override
    public void setOff() {
        writeSerial("{105}");

    }

    @Override
    public void setPresenceMode() {
        writeSerial("{107}");

    }

    @Override
    public void setCalendarMode() {
        writeSerial("{108}");

    }

    @Override
    public void setCalendarDailyTemperatureRange(int weekDay, int rangeIndex, int startHour, int startMinute, int stopHour, int stopMinute) {
        // {102;0;7;0;7;30;11;0;20.5}
        StringBuilder builder = new StringBuilder("{102;0;");
        builder.append(weekDay).append(";").append(rangeIndex).append(";").append(startHour).append(";").append(startMinute).append(";")
                .append(stopHour).append(";").append(stopMinute).append("}");
        writeSerial(builder.toString());

    }

    @Override
    public void setCalendarDefaultDailyTemp(int weekDay, float defaultTemp) {
        // default day temp
        // {4;0;7;17.5};
        StringBuilder builder = new StringBuilder("{103;0;");
        builder.append(weekDay).append(";").append(defaultTemp).append("}");
        writeSerial(builder.toString());

    }

    /**
     * {12; comfort; default; night}
     */
    @Override
    public void setPresenceTemperatures(float comfortTemp, float defaultTemp, float nightTemp) {
        StringBuilder builder = new StringBuilder("{109;");
        builder.append(comfortTemp).append(";").append(defaultTemp).append(";").append(nightTemp).append("}");
        writeSerial(builder.toString());
    }

    @Override
    public void setPresenceDetected() {
        writeSerial("{3;1}");
    }

    @Override
    public void registerListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties != null) {
            initialized = false;
            Enumeration<String> keys = properties.keys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                logger.debug(key + ": " + properties.get(key));
            }
            port = (String) properties.get("port");

            if (port == null) {
                logger.error("Port not set!!!");
                return;
            }

            serialPort = new SerialPort(port);
            try {
                serialPort.openPort();// Open serial port
                serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                int mask = SerialPort.MASK_RXCHAR;// Prepare mask
                serialPort.setEventsMask(mask);// Set mask

                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent event) {
                        if (event.isRXCHAR()) {// If data is available
                            if (event.getEventValue() > 0) {// Check bytes count in
                                                            // the input buffer
                                // Read data, if 10 bytes available
                                try {
                                    String data = serialPort.readString(event.getEventValue());
                                    processData(data);
                                } catch (SerialPortException ex) {
                                    System.out.println(ex);
                                }
                            }
                        }
                    }
                });
                initialized = true;
            } catch (SerialPortException ex) {
                logger.error("Exception while opening serial port", ex);
            }

        } else {
            logger.warn("Empty configuration passed to IHomeCommunicationController");
            // throw new RuntimeException("Configuration required for com.jwdevs.ihome!");
        }
    }

    @Override
    public void serverHeartBeat() {
        // TODO Auto-generated method stub

    }
}
