package com.arv.framework.adk.tools;

import com.arv.framework.adk.interfaces.tool.IToolInput;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of IToolInput for tool execution input.
 * Contains parameters, session ID, and context for tool execution.
 */
public class ToolInput implements IToolInput {
  
  private Map<String, Object> parameters;
  private String sessionId;
  private Map<String, Object> context;
  
  /**
   * Default constructor for ToolInput.
   */
  public ToolInput() {
    this.parameters = new HashMap<>();
    this.context = new HashMap<>();
  }
  
  /**
   * Constructor with session ID.
   * 
   * @param sessionId the session ID
   */
  public ToolInput(String sessionId) {
    this();
    this.sessionId = sessionId;
  }
  
  @Override
  public Map<String, Object> getParameters() {
    return new HashMap<>(parameters);
  }
  
  @Override
  public void setParameters(Map<String, Object> parameters) {
    this.parameters = new HashMap<>(parameters);
  }
  
  @Override
  public Object getParameter(String key) {
    return parameters.get(key);
  }
  
  @Override
  public void setParameter(String key, Object value) {
    parameters.put(key, value);
  }
  
  @Override
  public String getSessionId() {
    return sessionId;
  }
  
  @Override
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  @Override
  public Map<String, Object> getContext() {
    return new HashMap<>(context);
  }
  
  @Override
  public void setContext(Map<String, Object> context) {
    this.context = new HashMap<>(context);
  }
  
  @Override
  public Object getContextValue(String key) {
    return context.get(key);
  }
  
  @Override
  public void setContextValue(String key, Object value) {
    context.put(key, value);
  }
  
  @Override
  public boolean hasParameter(String key) {
    return parameters.containsKey(key);
  }
  
  @Override
  public boolean hasContextValue(String key) {
    return context.containsKey(key);
  }
  
  /**
   * Gets a parameter as String.
   * 
   * @param key the parameter key
   * @return the parameter value as String, or null if not found
   */
  public String getParameterAsString(String key) {
    Object value = getParameter(key);
    return value != null ? value.toString() : null;
  }
  
  /**
   * Gets a parameter as Integer.
   * 
   * @param key the parameter key
   * @return the parameter value as Integer, or null if not found or not a number
   */
  public Integer getParameterAsInteger(String key) {
    Object value = getParameter(key);
    if (value instanceof Integer) {
      return (Integer) value;
    } else if (value instanceof Number) {
      return ((Number) value).intValue();
    } else if (value instanceof String) {
      try {
        return Integer.parseInt((String) value);
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return null;
  }
  
  /**
   * Gets a parameter as Double.
   * 
   * @param key the parameter key
   * @return the parameter value as Double, or null if not found or not a number
   */
  public Double getParameterAsDouble(String key) {
    Object value = getParameter(key);
    if (value instanceof Double) {
      return (Double) value;
    } else if (value instanceof Number) {
      return ((Number) value).doubleValue();
    } else if (value instanceof String) {
      try {
        return Double.parseDouble((String) value);
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return null;
  }
}
