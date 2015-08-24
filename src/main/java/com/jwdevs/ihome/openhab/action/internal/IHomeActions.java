/** * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab.action.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwdevs.ihome.openhab.communication.IHomeCommunicationController;

/**
 * This class contains the methods that are made available in scripts and rules for IHomeActions.
 * 
 * @author jwozniak
 * @since 1.0.0
 */
public class IHomeActions {

    private static final Logger logger = LoggerFactory.getLogger(IHomeActions.class);
    private static IHomeCommunicationController controller;

    public static void setCommunicationController(IHomeCommunicationController ctr) {
        controller = ctr;
    }

    public static void unsetCommunicationController(IHomeCommunicationController ctr) {
        controller = null;
    }

    // provide public static methods here

    // Example
    @ActionDoc(text = "Sending the comfort, low and night temperatures to the heater controller", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean sendPresenceTemperaturesToHeater(@ParamDoc(name = "comfortTemp", text = "Comfort temperature") float comfortTemp,
            @ParamDoc(name = "defaultTemp", text = "Low temperature") float defaultTemp,
            @ParamDoc(name = "nightTemp", text = "Night temperature") float nightTemp) {
        if (!IHomeActionService.isProperlyConfigured) {
            logger.debug("IHomeActions action is not yet configured - execution aborted!");
            return false;
        }
        IHomeCommunicationController controller = getCommunicationController();
        controller.setPresenceTemperatures(comfortTemp, defaultTemp, nightTemp);
        return true;
    }

    public static void setPresenceMode() {
        if (!IHomeActionService.isProperlyConfigured) {
            logger.debug("IHomeActions action is not yet configured - execution aborted!");
            throw new RuntimeException("Action not configured");
        }
        IHomeCommunicationController controller = getCommunicationController();
        controller.setPresenceMode();
    }

    public static void setCalendarMode() {
        if (!IHomeActionService.isProperlyConfigured) {
            logger.debug("IHomeActions action is not yet configured - execution aborted!");
            throw new RuntimeException("Action not configured");
        }
        IHomeCommunicationController controller = getCommunicationController();
        controller.setCalendarMode();
    }

    public static void setPresenceDetected() {
        if (!IHomeActionService.isProperlyConfigured) {
            logger.debug("IHomeActions action is not yet configured - execution aborted!");
            throw new RuntimeException("Action not configured");
        }
        IHomeCommunicationController controller = getCommunicationController();
        controller.setPresenceDetected();
    }

    private static IHomeCommunicationController getCommunicationController() {
        /*
         * ServiceReference<IHomeCommunicationController> ref =
         * IHomeActivator.getContext().getServiceReference(IHomeCommunicationController.class); return
         * IHomeActivator.getContext().getService(ref);
         */
        if (controller == null) {
            throw new RuntimeException("Communication controller is not set!");
        }
        return controller;
    }
}
