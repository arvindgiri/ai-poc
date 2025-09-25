package com.arv.framework.adk.interfaces.tool;

import java.util.Map;

/**
 * Input data structure for tool execution.
 * This interface defines the input structure for tools in the ConnectADK framework.
 */
public interface IToolInput {
    
    /**
     * Gets the parameters for the tool execution.
     * 
     * @return the parameters map
     */
    Map<String, Object> getParameters();
    
    /**
     * Sets the parameters for the tool execution.
     * 
     * @param parameters the parameters map
     */
    void setParameters(Map<String, Object> parameters);
    
    /**
     * Gets a specific parameter by key.
     * 
     * @param key the parameter key
     * @return the parameter value
     */
    Object getParameter(String key);
    
    /**
     * Sets a specific parameter.
     * 
     * @param key the parameter key
     * @param value the parameter value
     */
    void setParameter(String key, Object value);
    
    /**
     * Gets the session ID associated with this input.
     * 
     * @return the session ID
     */
    String getSessionId();
    
    /**
     * Sets the session ID for this input.
     * 
     * @param sessionId the session ID
     */
    void setSessionId(String sessionId);
    
    /**
     * Gets the context associated with this input.
     * 
     * @return the context map
     */
    Map<String, Object> getContext();
    
    /**
     * Sets the context for this input.
     * 
     * @param context the context map
     */
    void setContext(Map<String, Object> context);
    
    /**
     * Gets a specific context value by key.
     * 
     * @param key the context key
     * @return the context value
     */
    Object getContextValue(String key);
    
    /**
     * Sets a specific context value.
     * 
     * @param key the context key
     * @param value the context value
     */
    void setContextValue(String key, Object value);
    
    /**
     * Checks if a parameter exists.
     * 
     * @param key the parameter key
     * @return true if parameter exists, false otherwise
     */
    boolean hasParameter(String key);
    
    /**
     * Checks if a context value exists.
     * 
     * @param key the context key
     * @return true if context value exists, false otherwise
     */
    boolean hasContextValue(String key);
}
