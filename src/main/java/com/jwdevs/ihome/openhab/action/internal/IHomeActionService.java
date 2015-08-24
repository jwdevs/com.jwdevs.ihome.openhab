/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab.action.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwdevs.ihome.openhab.communication.IHomeCommunicationController;

/**
 * This class registers an OSGi service for the IHomeActions action.
 * 
 * @author jwozniak
 * @since 1.0.0
 */
public class IHomeActionService implements org.openhab.core.scriptengine.action.ActionService, ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(IHomeActionService.class);
    private IHomeCommunicationController controller;

    /**
     * Indicates whether this action is properly configured which means all necessary configurations are set. This flag can be checked by
     * the action methods before executing code.
     */
    /* default */static boolean isProperlyConfigured = true;

    public IHomeActionService() {
    }

    public void setCommunicationController(IHomeCommunicationController controller) {
        logger.debug("Setting communication controller {}", controller.hashCode());
        this.controller = controller;
        IHomeActions.setCommunicationController(controller);
    }

    public void unsetCommunicationController(IHomeCommunicationController controller) {
        logger.debug("Removing communication controller {}", controller.hashCode());
        this.controller = null;
        IHomeActions.unsetCommunicationController(controller);
    }

    public void activate() {
    }

    public void deactivate() {
        // deallocate Resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return IHomeActions.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return IHomeActions.class;
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            // read config parameters here ...

            isProperlyConfigured = true;
        }
    }

}
