package com.arv.framework.adk.interfaces.tool;

import java.util.Map;

/**
 * Factory for creating tool instances.
 * This interface defines the contract for tool creation in the ConnectADK framework.
 */
public interface IToolFactory {
    
    /**
     * Creates a tool instance from a class.
     * 
     * @param toolClass the tool class
     * @return the created tool instance
     */
    ITool createTool(Class<? extends ITool> toolClass);
    
    /**
     * Creates a tool instance from configuration.
     * 
     * @param config the tool configuration
     * @return the created tool instance
     */
    ITool createTool(Map<String, Object> config);
    
    /**
     * Creates a tool instance by type.
     * 
     * @param toolType the tool type
     * @return the created tool instance
     */
    ITool createTool(String toolType);
    
    /**
     * Registers a tool creator for a specific type.
     * 
     * @param toolType the tool type
     * @param creator the tool creator
     */
    void registerToolCreator(String toolType, IToolCreator creator);
    
    /**
     * Unregisters a tool creator.
     * 
     * @param toolType the tool type
     */
    void unregisterToolCreator(String toolType);
    
    /**
     * Gets the available tool types.
     * 
     * @return the list of available tool types
     */
    java.util.List<String> getAvailableToolTypes();
    
    /**
     * Checks if a tool type is supported.
     * 
     * @param toolType the tool type
     * @return true if supported, false otherwise
     */
    boolean isToolTypeSupported(String toolType);
    
    /**
     * Gets the tool metadata for a type.
     * 
     * @param toolType the tool type
     * @return the tool metadata
     */
    IToolMetadata getToolMetadata(String toolType);
}
