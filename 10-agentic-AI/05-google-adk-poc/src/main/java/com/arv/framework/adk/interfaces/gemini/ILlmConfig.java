package com.arv.framework.adk.interfaces.gemini;

import java.util.Map;

/**
 * Configuration interface for Large Language Model (LLM) services.
 * This interface defines the configuration contract for LLM integration in the ConnectADK framework.
 */
public interface ILlmConfig {
    
    /**
     * Gets the LLM API key.
     * 
     * @return the API key
     */
    String getApiKey();
    
    /**
     * Sets the LLM API key.
     * 
     * @param apiKey the API key
     */
    void setApiKey(String apiKey);
    
    /**
     * Gets the LLM API base URL.
     * 
     * @return the API base URL
     */
    String getApiUrl();
    
    /**
     * Sets the LLM API base URL.
     * 
     * @param apiUrl the API base URL
     */
    void setApiUrl(String apiUrl);
    
    /**
     * Gets the LLM model name.
     * 
     * @return the model name
     */
    String getModel();
    
    /**
     * Sets the LLM model name.
     * 
     * @param model the model name
     */
    void setModel(String model);
    
    /**
     * Gets the temperature for response generation.
     * 
     * @return the temperature value
     */
    double getTemperature();
    
    /**
     * Sets the temperature for response generation.
     * 
     * @param temperature the temperature value
     */
    void setTemperature(double temperature);
    
    /**
     * Gets the maximum tokens for response generation.
     * 
     * @return the maximum tokens
     */
    int getMaxTokens();
    
    /**
     * Sets the maximum tokens for response generation.
     * 
     * @param maxTokens the maximum tokens
     */
    void setMaxTokens(int maxTokens);
    
    /**
     * Gets the request timeout in milliseconds.
     * 
     * @return the timeout in milliseconds
     */
    long getTimeoutMs();
    
    /**
     * Sets the request timeout in milliseconds.
     * 
     * @param timeoutMs the timeout in milliseconds
     */
    void setTimeoutMs(long timeoutMs);
    
    /**
     * Gets the maximum number of retry attempts.
     * 
     * @return the maximum retry attempts
     */
    int getMaxRetries();
    
    /**
     * Sets the maximum number of retry attempts.
     * 
     * @param maxRetries the maximum retry attempts
     */
    void setMaxRetries(int maxRetries);
    
    /**
     * Gets additional configuration properties.
     * 
     * @return the properties map
     */
    Map<String, Object> getProperties();
    
    /**
     * Sets additional configuration properties.
     * 
     * @param properties the properties map
     */
    void setProperties(Map<String, Object> properties);
    
    /**
     * Gets a specific property by key.
     * 
     * @param key the property key
     * @return the property value
     */
    Object getProperty(String key);
    
    /**
     * Sets a specific property.
     * 
     * @param key the property key
     * @param value the property value
     */
    void setProperty(String key, Object value);
    
    /**
     * Validates the configuration.
     * 
     * @return true if configuration is valid, false otherwise
     */
    boolean isValid();
    
    /**
     * Gets the provider name (e.g., "OpenAI", "Google", "Anthropic").
     * 
     * @return the provider name
     */
    String getProvider();
    
    /**
     * Sets the provider name.
     * 
     * @param provider the provider name
     */
    void setProvider(String provider);
    
    /**
     * Checks if the configuration is initialized.
     * 
     * @return true if initialized, false otherwise
     */
    boolean isInitialized();
    
    /**
     * Creates a copy of this configuration.
     * 
     * @return a copy of the configuration
     */
    ILlmConfig copy();
}
