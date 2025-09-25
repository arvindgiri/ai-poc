package com.arv.framework.adk.tools;

import com.arv.framework.adk.interfaces.tool.IToolResult;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of IToolResult for tool execution results.
 * Contains execution results, success status, and metadata.
 */
public class ToolResult implements IToolResult {
  
  private boolean success;
  private String data;
  private String error;
  private Map<String, Object> metadata;
  private long executionTime;
  private long timestamp;
  
  /**
   * Default constructor for ToolResult.
   */
  public ToolResult() {
    this.success = true;
    this.metadata = new HashMap<>();
    this.timestamp = System.currentTimeMillis();
  }
  
  /**
   * Constructor with success status and data.
   * 
   * @param success the success status
   * @param data the result data
   */
  public ToolResult(boolean success, String data) {
    this();
    this.success = success;
    this.data = data;
  }
  
  /**
   * Creates a successful result with data.
   * 
   * @param data the result data
   * @return a successful ToolResult
   */
  public static ToolResult success(String data) {
    return new ToolResult(true, data);
  }
  
  /**
   * Creates a failed result with error message.
   * 
   * @param error the error message
   * @return a failed ToolResult
   */
  public static ToolResult failure(String error) {
    ToolResult result = new ToolResult(false, null);
    result.setError(error);
    return result;
  }
  
  @Override
  public boolean isSuccess() {
    return success;
  }
  
  @Override
  public void setSuccess(boolean success) {
    this.success = success;
  }
  
  @Override
  public String getData() {
    return data;
  }
  
  @Override
  public void setData(String data) {
    this.data = data;
  }
  
  @Override
  public String getError() {
    return error;
  }
  
  @Override
  public void setError(String error) {
    this.error = error;
    this.success = false; // Set success to false when error is set
  }
  
  @Override
  public boolean hasError() {
    return error != null && !error.trim().isEmpty();
  }
  
  @Override
  public Map<String, Object> getMetadata() {
    return new HashMap<>(metadata);
  }
  
  @Override
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = new HashMap<>(metadata);
  }
  
  @Override
  public Object getMetadataValue(String key) {
    return metadata.get(key);
  }
  
  @Override
  public void setMetadataValue(String key, Object value) {
    metadata.put(key, value);
  }
  
  @Override
  public long getExecutionTime() {
    return executionTime;
  }
  
  @Override
  public void setExecutionTime(long executionTime) {
    this.executionTime = executionTime;
  }
  
  @Override
  public long getTimestamp() {
    return timestamp;
  }
  
  @Override
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
  
  /**
   * Adds metadata to the result.
   * 
   * @param key the metadata key
   * @param value the metadata value
   * @return this ToolResult for method chaining
   */
  public ToolResult withMetadata(String key, Object value) {
    setMetadataValue(key, value);
    return this;
  }
  
  /**
   * Sets the execution time based on start time.
   * 
   * @param startTime the start time in milliseconds
   * @return this ToolResult for method chaining
   */
  public ToolResult withExecutionTime(long startTime) {
    setExecutionTime(System.currentTimeMillis() - startTime);
    return this;
  }
}
