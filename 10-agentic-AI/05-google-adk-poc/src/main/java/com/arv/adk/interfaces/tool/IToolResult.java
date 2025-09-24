package com.arv.adk.interfaces.tool;

import java.util.Map;

/**
 * Output data structure from tool execution.
 * This interface defines the result structure for tools in the ConnectADK framework.
 */
public interface IToolResult {
    
    /**
     * Checks if the tool execution was successful.
     * 
     * @return true if successful, false otherwise
     */
    boolean isSuccess();
    
    /**
     * Sets the success status of the tool execution.
     * 
     * @param success the success status
     */
    void setSuccess(boolean success);
    
    /**
     * Gets the data returned by the tool.
     * 
     * @return the tool data
     */
    String getData();
    
    /**
     * Sets the data returned by the tool.
     * 
     * @param data the tool data
     */
    void setData(String data);
    
    /**
     * Gets the error message if execution failed.
     * 
     * @return the error message
     */
    String getError();
    
    /**
     * Sets the error message.
     * 
     * @param error the error message
     */
    void setError(String error);
    
    /**
     * Checks if there was an error.
     * 
     * @return true if there was an error, false otherwise
     */
    boolean hasError();
    
    /**
     * Gets the metadata associated with the result.
     * 
     * @return the metadata map
     */
    Map<String, Object> getMetadata();
    
    /**
     * Sets the metadata for the result.
     * 
     * @param metadata the metadata map
     */
    void setMetadata(Map<String, Object> metadata);
    
    /**
     * Gets a specific metadata value by key.
     * 
     * @param key the metadata key
     * @return the metadata value
     */
    Object getMetadataValue(String key);
    
    /**
     * Sets a specific metadata value.
     * 
     * @param key the metadata key
     * @param value the metadata value
     */
    void setMetadataValue(String key, Object value);
    
    /**
     * Gets the execution time in milliseconds.
     * 
     * @return the execution time
     */
    long getExecutionTime();
    
    /**
     * Sets the execution time.
     * 
     * @param executionTime the execution time in milliseconds
     */
    void setExecutionTime(long executionTime);
    
    /**
     * Gets the timestamp when the tool was executed.
     * 
     * @return the execution timestamp
     */
    long getTimestamp();
    
    /**
     * Sets the execution timestamp.
     * 
     * @param timestamp the execution timestamp
     */
    void setTimestamp(long timestamp);
}
