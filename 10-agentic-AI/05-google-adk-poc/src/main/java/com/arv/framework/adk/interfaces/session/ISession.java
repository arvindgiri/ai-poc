package com.arv.framework.adk.interfaces.session;

import com.arv.framework.adk.interfaces.enums.SessionState;
import java.util.List;
import java.util.Map;

/**
 * Represents a user session with context and history.
 * This interface defines the structure of user sessions in the ConnectADK framework.
 */
public interface ISession {
    
    /**
     * Gets the unique session ID.
     * 
     * @return the session ID
     */
    String getId();
    
    /**
     * Gets the user ID associated with this session.
     * 
     * @return the user ID
     */
    String getUserId();
    
    /**
     * Sets the user ID for this session.
     * 
     * @param userId the user ID
     */
    void setUserId(String userId);
    
    /**
     * Gets the current state of the session.
     * 
     * @return the session state
     */
    SessionState getState();
    
    /**
     * Sets the state of the session.
     * 
     * @param state the session state
     */
    void setState(SessionState state);
    
    /**
     * Gets the session context.
     * 
     * @return the context map
     */
    Map<String, Object> getContext();
    
    /**
     * Sets the session context.
     * 
     * @param context the context map
     */
    void setContext(Map<String, Object> context);
    
    /**
     * Updates the session context with new values.
     * 
     * @param updates the context updates
     */
    void updateContext(Map<String, Object> updates);
    
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
     * Gets the message history for this session.
     * 
     * @return the list of messages
     */
    List<IMessage> getMessageHistory();
    
    /**
     * Adds a message to the session history.
     * 
     * @param message the message to add
     */
    void addMessage(IMessage message);
    
    /**
     * Gets the number of messages in this session.
     * 
     * @return the message count
     */
    int getMessageCount();
    
    /**
     * Gets the creation timestamp of the session.
     * 
     * @return the creation timestamp
     */
    long getCreatedAt();
    
    /**
     * Gets the last activity timestamp of the session.
     * 
     * @return the last activity timestamp
     */
    long getLastActivityAt();
    
    /**
     * Updates the last activity timestamp.
     */
    void updateLastActivity();
    
    /**
     * Checks if the session is active.
     * 
     * @return true if active, false otherwise
     */
    boolean isActive();
    
    /**
     * Activates the session.
     */
    void activate();
    
    /**
     * Deactivates the session.
     */
    void deactivate();
    
    /**
     * Clears the session history.
     */
    void clearHistory();
    
    /**
     * Gets the session metadata.
     * 
     * @return the metadata map
     */
    Map<String, Object> getMetadata();
    
    /**
     * Sets the session metadata.
     * 
     * @param metadata the metadata map
     */
    void setMetadata(Map<String, Object> metadata);
}
