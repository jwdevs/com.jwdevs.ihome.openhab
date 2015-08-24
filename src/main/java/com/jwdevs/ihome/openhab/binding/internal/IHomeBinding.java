/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab.binding.internal;

import groovy.lang.Script;

import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwdevs.ihome.openhab.binding.IHomeBindingProvider;
import com.jwdevs.ihome.openhab.communication.IHomeCommunicationController;
import com.jwdevs.ihome.openhab.message.HeaterMessage;
import com.jwdevs.ihome.openhab.message.HumidityMessage;
import com.jwdevs.ihome.openhab.message.MessageListener;
import com.jwdevs.ihome.openhab.message.PirMessage;
import com.jwdevs.ihome.openhab.message.RemoteMessage;
import com.jwdevs.ihome.openhab.message.ThermometerMessage;
import com.jwdevs.ihome.openhab.message.ThermostatDiagnosticsMessage;

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author jwozniak
 * @since 0.1.0
 */
public class IHomeBinding extends AbstractActiveBinding<IHomeBindingProvider> implements ManagedService, MessageListener {
    private IHomeCommunicationController controller;

    private static final Logger logger = LoggerFactory.getLogger(IHomeBinding.class);

    /**
     * the refresh interval which is used to poll values from the jwxbee server (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    public IHomeBinding() {

    }

    public void setCommunicationController(IHomeCommunicationController controller) {
        logger.debug("Setting communication controller {}", controller.hashCode());
        this.controller = controller;
        this.controller.registerListener(this);
        logger.debug("Communication controller succesfully acquired from context");
    }

    public void unsetCommunicationController(IHomeCommunicationController controller) {
        logger.debug("Removing communication controller {}", controller.hashCode());
        this.controller = null;
    }

    @Override
    public void onHeaterMessage(HeaterMessage msg) {
        onRemoteMessage(msg);
    }

    @Override
    public void onThermometerMessage(ThermometerMessage msg) {
        onRemoteMessage(msg);
    }

    @Override
    public void onPirMessage(PirMessage msg) {
        onRemoteMessage(msg);
    }

    @Override
    public void onHumidityMessage(HumidityMessage msg) {
        onRemoteMessage(msg);
    }

    @Override
    public void onThermostatDiagnosticsMessage(ThermostatDiagnosticsMessage msg) {
        onRemoteMessage(msg);
    }

    private void onRemoteMessage(RemoteMessage msg) {
        String className = msg.getClass().getCanonicalName();
        logger.debug("Message " + className + " in binding, providers " + providers);

        for (IHomeBindingProvider provider : providers) {
            List<IHomeBindingConfig> configs = provider.getBindingConfigsForMessageType(className);
            for (IHomeBindingConfig config : configs) {
                logger.debug("Checking item " + config.getItemName());
                if (executeWhenCondition(config.getWhenScript(), msg)) {
                    Object value = executeValueCondition(config.getValueScript(), msg);
                    State state = null;
                    if (value instanceof Number) {
                        state = new DecimalType(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        state = ((Boolean) value) == true ? OnOffType.ON : OnOffType.OFF;
                    } else if (value instanceof String) {
                        state = new StringType((String) value);
                    } else {
                        logger.error("Unsupported type {}", value);
                    }
                    if (state != null) {
                        eventPublisher.postUpdate(config.getItemName(), state);
                    }
                } else {
                    logger.debug("Message did not pass when condition");
                }
            }

        }
    }

    private Object executeValueCondition(Script valueScript, RemoteMessage msg) {
        valueScript.getBinding().setVariable("msg", msg);
        Object value = valueScript.run();
        logger.debug("Value: " + value);
        return value;
    }

    private boolean executeWhenCondition(Script whenScript, RemoteMessage msg) {
        whenScript.getBinding().setVariable("msg", msg);
        Object ret = whenScript.run();
        logger.debug("When value: " + ret);
        if (ret instanceof Boolean) {
            return ((Boolean) ret).booleanValue();
        } else {
            throw new RuntimeException("Wrong configuration of script, boolean return expected but received "
                    + ret.getClass().getCanonicalName() + ", script: " + whenScript.toString());
        }
    }

    @Override
    public void activate() {
        /*
         * ServiceReference<IHomeCommunicationController> ref = IHomeActivator.getContext().getServiceReference(
         * IHomeCommunicationController.class); this.controller = IHomeActivator.getContext().getService(ref);
         * this.controller.registerListener(this); logger.debug("Communication controller succesfully acquired from context");
         */
    }

    @Override
    public void deactivate() {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected String getName() {
        return "jwxbee Refresh Service";
    }

    @Override
    protected void execute() {
        // the frequently executed code (polling) goes here ...
        logger.debug("execute() method is called!");

        // eventPublisher.postUpdate("Heater", OnOffType.ON);

    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.

        logger.debug("internalReceiveCommand() is called with item={}, command={}", itemName, command);
        /*
         * if (itemName.equals("Heater") && command instanceof OnOffType) { OnOffType comm = (OnOffType) command; if
         * (OnOffType.ON.equals(comm)) { controller.setOn(); } else { controller.setOff();
         * 
         * } }
         */
        for (IHomeBindingProvider provider : providers) {
            IHomeBindingConfig config = provider.getBindingConfigForItem(itemName);
            logger.debug("Checking item " + config.getItemName());
            if (config.getCommandScript() != null) {
                executeCommandScript(config.getCommandScript(), command);
            }
        }

    }

    private void executeCommandScript(Script commandScript, Command command) {
        commandScript.getBinding().setVariable("value", command);
        commandScript.getBinding().setVariable("ctrl", this.controller);
        commandScript.run();
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.

        logger.debug("internalReceiveUpdate() is called with item={}, newState={}", itemName, newState);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            // to override the default refresh interval one has to add a
            // parameter to openhab.cfg like
            // <bindingName>:refresh=<intervalInMs>
            String refreshIntervalString = (String) config.get("refresh");
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            // read further config parameters here ...

            setProperlyConfigured(true);
        }
    }

}
