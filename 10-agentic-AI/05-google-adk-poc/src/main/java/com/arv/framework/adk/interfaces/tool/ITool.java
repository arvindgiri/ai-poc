package com.arv.framework.adk.interfaces.tool;

/**
 * Interface that all tools must implement (contains business logic).
 * This interface defines the contract for tools in the ConnectADK framework.
 */
public interface ITool {
    
    /**
     * Gets the unique identifier of the tool.
     * 
     * @return the tool ID
     */
    String getId();
    
    /**
     * Gets the name of the tool.
     * 
     * @return the tool name
     */
    String getName();
    
    /**
     * Gets the description of the tool.
     * 
     * @return the tool description
     */
    String getDescription();
    
    /**
     * Executes the tool with the given input.
     * 
     * @param input the tool input
     * @return the tool result
     */
    IToolResult execute(IToolInput input);
    
    /**
     * Checks if the tool is enabled.
     * 
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();
    
    /**
     * Sets the enabled state of the tool.
     * 
     * @param enabled the enabled state
     */
    void setEnabled(boolean enabled);
    
    /**
     * Gets the tool metadata.
     * 
     * @return the tool metadata
     */
    IToolMetadata getMetadata();
    
    /**
     * Validates the tool input.
     * 
     * @param input the tool input to validate
     * @return true if input is valid, false otherwise
     */
    boolean validateInput(IToolInput input);
}
