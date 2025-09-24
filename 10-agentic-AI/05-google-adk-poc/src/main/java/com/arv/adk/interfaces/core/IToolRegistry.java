package com.arv.adk.interfaces.core;

import com.arv.adk.interfaces.tool.ITool;
import com.arv.adk.interfaces.tool.IToolMetadata;
import java.util.List;
import java.util.Optional;

/**
 * Manages tool registration, retrieval, and lifecycle operations.
 * This interface provides methods for managing tools in the ConnectADK framework.
 */
public interface IToolRegistry {
    
    /**
     * Registers a tool with the registry.
     * 
     * @param tool the tool to register
     * @return true if registration was successful, false otherwise
     */
    boolean registerTool(ITool tool);
    
    /**
     * Unregisters a tool from the registry.
     * 
     * @param toolId the ID of the tool to unregister
     * @return true if unregistration was successful, false otherwise
     */
    boolean unregisterTool(String toolId);
    
    /**
     * Gets a tool by its ID.
     * 
     * @param toolId the tool ID
     * @return an Optional containing the tool if found, empty otherwise
     */
    Optional<ITool> getTool(String toolId);
    
    /**
     * Gets all registered tools.
     * 
     * @return a list of all tools
     */
    List<ITool> getAllTools();
    
    /**
     * Gets all enabled tools.
     * 
     * @return a list of enabled tools
     */
    List<ITool> getEnabledTools();
    
    /**
     * Gets all disabled tools.
     * 
     * @return a list of disabled tools
     */
    List<ITool> getDisabledTools();
    
    /**
     * Enables a tool.
     * 
     * @param toolId the tool ID
     * @return true if tool was enabled successfully, false otherwise
     */
    boolean enableTool(String toolId);
    
    /**
     * Disables a tool.
     * 
     * @param toolId the tool ID
     * @return true if tool was disabled successfully, false otherwise
     */
    boolean disableTool(String toolId);
    
    /**
     * Checks if a tool is registered.
     * 
     * @param toolId the tool ID
     * @return true if tool is registered, false otherwise
     */
    boolean isToolRegistered(String toolId);
    
    /**
     * Checks if a tool is enabled.
     * 
     * @param toolId the tool ID
     * @return true if tool is enabled, false otherwise
     */
    boolean isToolEnabled(String toolId);
    
    /**
     * Gets tool metadata by ID.
     * 
     * @param toolId the tool ID
     * @return an Optional containing the metadata if found, empty otherwise
     */
    Optional<IToolMetadata> getToolMetadata(String toolId);
    
    /**
     * Gets the number of registered tools.
     * 
     * @return the number of tools
     */
    int getToolCount();
}
