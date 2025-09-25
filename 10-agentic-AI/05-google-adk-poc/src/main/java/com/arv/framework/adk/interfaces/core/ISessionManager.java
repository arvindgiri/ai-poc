package com.arv.framework.adk.interfaces.core;

import com.arv.framework.adk.interfaces.session.ISession;
import com.arv.framework.adk.interfaces.session.IMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handles session creation, management, and cleanup.
 * This interface provides methods for managing user sessions in the ConnectADK framework.
 */
public interface ISessionManager {
    
    /**
     * Creates a new session.
     * 
     * @param sessionId the session ID
     * @return the created session
     */
    ISession createSession(String sessionId);
    
    /**
     * Creates a new session with a user ID.
     * 
     * @param sessionId the session ID
     * @param userId the user ID
     * @return the created session
     */
    ISession createSession(String sessionId, String userId);
    
    /**
     * Gets a session by ID.
     * 
     * @param sessionId the session ID
     * @return an Optional containing the session if found, empty otherwise
     */
    Optional<ISession> getSession(String sessionId);
    
    /**
     * Ends a session.
     * 
     * @param sessionId the session ID
     * @return true if session was ended successfully, false otherwise
     */
    boolean endSession(String sessionId);
    
    /**
     * Checks if a session is active.
     * 
     * @param sessionId the session ID
     * @return true if session is active, false otherwise
     */
    boolean isSessionActive(String sessionId);
    
    /**
     * Updates the context of a session.
     * 
     * @param sessionId the session ID
     * @param context the new context
     * @return true if context was updated successfully, false otherwise
     */
    boolean updateSessionContext(String sessionId, Map<String, Object> context);
    
    /**
     * Gets the context of a session.
     * 
     * @param sessionId the session ID
     * @return the session context
     */
    Map<String, Object> getSessionContext(String sessionId);
    
    /**
     * Adds a message to the session history.
     * 
     * @param sessionId the session ID
     * @param message the message to add
     * @return true if message was added successfully, false otherwise
     */
    boolean addMessage(String sessionId, IMessage message);
    
    /**
     * Gets the message history for a session.
     * 
     * @param sessionId the session ID
     * @return the list of messages
     */
    List<IMessage> getSessionHistory(String sessionId);
    
    /**
     * Gets recent messages for a session.
     * 
     * @param sessionId the session ID
     * @param count the number of recent messages to retrieve
     * @return the list of recent messages
     */
    List<IMessage> getRecentMessages(String sessionId, int count);
    
    /**
     * Clears the message history for a session.
     * 
     * @param sessionId the session ID
     * @return true if history was cleared successfully, false otherwise
     */
    boolean clearHistory(String sessionId);
    
    /**
     * Gets the number of messages in a session.
     * 
     * @param sessionId the session ID
     * @return the message count
     */
    int getMessageCount(String sessionId);
    
    /**
     * Gets all active sessions.
     * 
     * @return the list of active sessions
     */
    List<ISession> getActiveSessions();
    
    /**
     * Cleans up expired sessions.
     * 
     * @return the number of sessions cleaned up
     */
    int cleanupExpiredSessions();
}
