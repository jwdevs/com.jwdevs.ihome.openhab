/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 * 
 * @author jwozniak
 * @since 0.1.0
 */
public final class IHomeActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(IHomeActivator.class);

    private static BundleContext context;

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        context = bc;
        // logger.debug("Activating bundle - Creating Communication Controller");
        // IHomeCommunicationController controller = new IHomeCommunicationControllerImpl("/dev/tty.usbserial-DA011GW6");
        // context.registerService(IHomeCommunicationController.class, controller, null);

    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        context = null;
        logger.debug("jwxbee binding has been stopped.");
    }

    /**
     * Returns the bundle context of this bundle
     * 
     * @return the bundle context
     */
    public static BundleContext getContext() {

        return context;
    }

}
