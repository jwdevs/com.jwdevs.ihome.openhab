/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab.binding;

import java.util.List;

import com.jwdevs.ihome.openhab.binding.internal.IHomeBindingConfig;

/**
 * @author jwozniak
 * @since 0.1.0
 */
public interface IHomeBindingProvider extends org.openhab.core.binding.BindingProvider {

    List<IHomeBindingConfig> getBindingConfigsForMessageType(String className);

    IHomeBindingConfig getBindingConfigForItem(String itemName);

}
