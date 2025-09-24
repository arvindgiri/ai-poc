package com.arv.adk.interfaces.core;

import com.arv.adk.interfaces.tool.ITool;
import com.arv.adk.interfaces.session.ISession;

/**
 * Main orchestrator that manages tools, sessions, and message processing.
 * This is the primary interface for the ConnectADK framework.
 */
public interface IAgent {
    
    /**
     * Registers a tool with the agent.
     * 
     * @param tool the tool to register
     * @return true if registration was successful, false otherwise
     */
    boolean registerTool(ITool tool);
    
    /**
     * Unregisters a tool from the agent.
     * 
     * @param toolId the ID of the tool to unregister
     * @return true if unregistration was successful, false otherwise
     */
    boolean unregisterTool(String toolId);
    
    /**
     * Processes a user message and returns a response.
     * 
     * @param message the user's message
     * @param sessionId the session ID
     * @return the agent's response
     */
    String processMessage(String message, String sessionId);
    
    /**
     * Starts a new session.
     * 
     * @param sessionId the session ID
     * @return the created session
     */
    ISession startSession(String sessionId);
    
    /**
     * Ends an existing session.
     * 
     * @param sessionId the session ID
     * @return true if session was ended successfully, false otherwise
     */
    boolean endSession(String sessionId);
    
    /**
     * Sets the Gemini API key for the agent.
     * 
     * @param apiKey the API key
     */
    void setGeminiApiKey(String apiKey);
    
    /**
     * Sets the Gemini model for the agent.
     * 
     * @param model the model name
     */
    void setGeminiModel(String model);
}
