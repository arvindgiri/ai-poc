package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.gemini.ILlmConfig;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of ILlmConfig for Gemini LLM.
 */
public class GeminiLlmConfig implements ILlmConfig {
  
  private String apiKey;
  private String apiUrl;
  private String model;
  private String provider;
  private long timeoutMs;
  private int maxTokens;
  private int maxRetries;
  private double temperature;
  private Map<String, Object> properties;
  private boolean initialized;
  
  public GeminiLlmConfig() {
    this.provider = "Google";
    this.apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/";
    this.model = "gemini-2.0-flash";
    this.timeoutMs = 30000L; // 30 seconds
    this.maxTokens = 1000;
    this.maxRetries = 3;
    this.temperature = 0.7;
    this.properties = new HashMap<>();
    this.initialized = false;
  }
  
  @Override
  public String getApiKey() {
    return apiKey;
  }
  
  @Override
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
  
  @Override
  public String getApiUrl() {
    return apiUrl;
  }
  
  @Override
  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }
  
  @Override
  public String getModel() {
    return model;
  }
  
  @Override
  public void setModel(String model) {
    this.model = model;
  }
  
  @Override
  public long getTimeoutMs() {
    return timeoutMs;
  }
  
  @Override
  public void setTimeoutMs(long timeoutMs) {
    this.timeoutMs = timeoutMs;
  }
  
  @Override
  public int getMaxTokens() {
    return maxTokens;
  }
  
  @Override
  public void setMaxTokens(int maxTokens) {
    this.maxTokens = maxTokens;
  }
  
  @Override
  public int getMaxRetries() {
    return maxRetries;
  }
  
  @Override
  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }
  
  @Override
  public double getTemperature() {
    return temperature;
  }
  
  @Override
  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }
  
  @Override
  public Map<String, Object> getProperties() {
    return new HashMap<>(properties);
  }
  
  @Override
  public void setProperties(Map<String, Object> properties) {
    this.properties.clear();
    if (properties != null) {
      this.properties.putAll(properties);
    }
  }
  
  @Override
  public Object getProperty(String key) {
    return properties.get(key);
  }
  
  @Override
  public void setProperty(String key, Object value) {
    if (key != null) {
      if (value != null) {
        properties.put(key, value);
      } else {
        properties.remove(key);
      }
    }
  }
  
  @Override
  public boolean isValid() {
    return apiKey != null && !apiKey.trim().isEmpty() && 
           model != null && !model.trim().isEmpty();
  }
  
  @Override
  public String getProvider() {
    return provider;
  }
  
  @Override
  public void setProvider(String provider) {
    this.provider = provider;
  }
  
  @Override
  public boolean isInitialized() {
    return initialized;
  }
  
  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }
  
  @Override
  public ILlmConfig copy() {
    GeminiLlmConfig copy = new GeminiLlmConfig();
    copy.setApiKey(this.apiKey);
    copy.setApiUrl(this.apiUrl);
    copy.setModel(this.model);
    copy.setProvider(this.provider);
    copy.setTimeoutMs(this.timeoutMs);
    copy.setMaxTokens(this.maxTokens);
    copy.setMaxRetries(this.maxRetries);
    copy.setTemperature(this.temperature);
    copy.setProperties(new HashMap<>(this.properties));
    copy.setInitialized(this.initialized);
    return copy;
  }
}
