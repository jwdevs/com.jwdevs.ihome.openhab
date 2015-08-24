package com.jwdevs.ihome.openhab.binding.internal;

import groovy.lang.Script;

public class IHomeBindingConfig implements org.openhab.core.binding.BindingConfig {
    private final String messageClassName;
    private final Script whenScript;
    private final Script commandScript;
    private final Script valueScript;
    private final String itemName;

    public String getMessageClassName() {
        return messageClassName;
    }

    public Script getWhenScript() {
        return whenScript;
    }

    public Script getValueScript() {
        return valueScript;
    }

    public String getItemName() {
        return itemName;
    }

    public Script getCommandScript() {
        return this.commandScript;
    }

    public IHomeBindingConfig(String itemName, String messageClassName, Script whenScript, Script valueScript, Script commandScript) {
        this.itemName = itemName;
        this.messageClassName = messageClassName;
        this.whenScript = whenScript;
        this.valueScript = valueScript;
        this.commandScript = commandScript;

    }

    public String getMessageClass() {
        // TODO Auto-generated method stub
        return this.messageClassName;
    }

}