/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.jwdevs.ihome.openhab.binding.internal;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jwdevs.ihome.openhab.binding.IHomeBindingProvider;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author jwozniak
 * @since 0.1.0
 */
public class IHomeBindingProviderImpl extends AbstractGenericBindingProvider implements IHomeBindingProvider {
    private static final Logger logger = LoggerFactory.getLogger(IHomeBindingProviderImpl.class);
    private final GroovyShell shell;

    private final Map<String, Map<String, IHomeBindingConfig>> configsPerMessageClassName = new HashMap<>();

    public IHomeBindingProviderImpl() {
        this.shell = new GroovyShell();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "ihome";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
        // throw new BindingConfigParseException("item '" + item.getName()
        // + "' is of type '" + item.getClass().getSimpleName()
        // +
        // "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
        // }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        String[] split = bindingConfig.split("\\|");

        if (split.length >= 3) {

            logger.debug("Processing configuration for item {}, config {} in context {}", item, bindingConfig, context);

            String clazz = "com.jwdevs.ihome.openhab.message." + split[0] + "Impl";

            Script whenScript = shell.parse(split[1]);
            Script valueScript = shell.parse(split[2]);
            Script commandScript = null;
            if (split.length >= 4) {
                commandScript = shell.parse(createCommandScriptCode(split[3]));
            }

            IHomeBindingConfig config = new IHomeBindingConfig(item.getName(), clazz, whenScript, valueScript, commandScript);
            addBindingConfig(item, config);

        }

    }

    String createCommandScriptCode(String script) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("import org.openhab.core.library.types.OnOffType;\n");
        buffer.append(script);
        return buffer.toString();
    }

    @Override
    protected void addBindingConfig(Item item, org.openhab.core.binding.BindingConfig config) {
        super.addBindingConfig(item, config);
        IHomeBindingConfig bindConfig = (IHomeBindingConfig) config;
        Map<String, IHomeBindingConfig> configs = this.configsPerMessageClassName.get(bindConfig.getMessageClass());
        if (configs == null) {
            configs = new HashMap<>();
            this.configsPerMessageClassName.put(bindConfig.getMessageClass(), configs);
        }
        configs.put(item.getName(), bindConfig);
    }

    @Override
    public List<IHomeBindingConfig> getBindingConfigsForMessageType(String className) {
        Map<String, IHomeBindingConfig> configs = this.configsPerMessageClassName.get(className);
        if (configs == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(configs.values());
        }
    }

    @Override
    public IHomeBindingConfig getBindingConfigForItem(String itemName) {
        return (IHomeBindingConfig) this.bindingConfigs.get(itemName);
    };

}
