package com.arv.adk.interfaces.gemini;

import java.util.Map;
import java.util.List;

/**
 * Handles all communication with Google's Gemini API.
 * This interface defines the contract for Gemini integration in the ConnectADK framework.
 */
public interface IGeminiService {
    
    /**
     * Initializes the Gemini service with API key and configuration.
     * 
     * @param apiKey the Gemini API key
     * @param config the service configuration
     */
    void initialize(String apiKey, Map<String, Object> config);
    
    /**
     * Generates a response from Gemini.
     * 
     * @param prompt the input prompt
     * @param context the context information
     * @return the generated response
     */
    String generateResponse(String prompt, Map<String, Object> context);
    
    /**
     * Analyzes user intent using Gemini.
     * 
     * @param message the user message
     * @param availableTools the list of available tools
     * @return the analyzed intent
     */
    String analyzeIntent(String message, List<String> availableTools);
    
    /**
     * Suggests tools based on user message.
     * 
     * @param message the user message
     * @param availableTools the list of available tools
     * @return the suggested tool IDs
     */
    List<String> suggestTools(String message, List<String> availableTools);
    
    /**
     * Extracts parameters for tool execution.
     * 
     * @param message the user message
     * @param toolMetadata the tool metadata
     * @return the extracted parameters
     */
    Map<String, Object> extractParameters(String message, String toolMetadata);
    
    /**
     * Validates a response from Gemini.
     * 
     * @param response the response to validate
     * @return true if response is valid, false otherwise
     */
    boolean validateResponse(String response);
    
    /**
     * Sets the Gemini model to use.
     * 
     * @param model the model name
     */
    void setModel(String model);
    
    /**
     * Gets the current Gemini model.
     * 
     * @return the model name
     */
    String getModel();
    
    /**
     * Sets the temperature for response generation.
     * 
     * @param temperature the temperature value
     */
    void setTemperature(double temperature);
    
    /**
     * Gets the current temperature setting.
     * 
     * @return the temperature value
     */
    double getTemperature();
    
    /**
     * Sets the maximum tokens for response generation.
     * 
     * @param maxTokens the maximum tokens
     */
    void setMaxTokens(int maxTokens);
    
    /**
     * Gets the current maximum tokens setting.
     * 
     * @return the maximum tokens
     */
    int getMaxTokens();
    
    /**
     * Checks if the service is initialized.
     * 
     * @return true if initialized, false otherwise
     */
    boolean isInitialized();
    
    /**
     * Gets the service status.
     * 
     * @return the service status
     */
    String getStatus();
    
    /**
     * Resets the service configuration.
     */
    void reset();
}
