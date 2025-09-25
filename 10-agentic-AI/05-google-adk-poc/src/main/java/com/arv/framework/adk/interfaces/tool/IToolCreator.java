package com.arv.framework.adk.interfaces.tool;

import java.util.Map;

/**
 * Interface for custom tool creation.
 * This interface defines the contract for tool creators in the ConnectADK framework.
 */
public interface IToolCreator {
    
    /**
     * Creates a tool instance.
     * 
     * @param config the tool configuration
     * @return the created tool instance
     */
    ITool createTool(Map<String, Object> config);
    
    /**
     * Gets the tool type this creator handles.
     * 
     * @return the tool type
     */
    String getToolType();
    
    /**
     * Validates the tool configuration.
     * 
     * @param config the tool configuration
     * @return true if configuration is valid, false otherwise
     */
    boolean validateConfig(Map<String, Object> config);
    
    /**
     * Gets the required configuration keys.
     * 
     * @return the list of required keys
     */
    java.util.List<String> getRequiredConfigKeys();
    
    /**
     * Gets the optional configuration keys.
     * 
     * @return the list of optional keys
     */
    java.util.List<String> getOptionalConfigKeys();
}
