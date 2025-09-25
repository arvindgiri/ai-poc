package com.arv.framework.adk.interfaces.enums;

/**
 * States of user sessions.
 * This enum defines the different states that a user session can be in within the ConnectADK framework.
 */
public enum SessionState {
    
    /**
     * Session is being created.
     */
    CREATING("Creating"),
    
    /**
     * Session is active and ready for use.
     */
    ACTIVE("Active"),
    
    /**
     * Session is paused.
     */
    PAUSED("Paused"),
    
    /**
     * Session is waiting for user input.
     */
    WAITING_FOR_INPUT("Waiting for Input"),
    
    /**
     * Session is processing a request.
     */
    PROCESSING("Processing"),
    
    /**
     * Session is idle.
     */
    IDLE("Idle"),
    
    /**
     * Session is being terminated.
     */
    TERMINATING("Terminating"),
    
    /**
     * Session has been terminated.
     */
    TERMINATED("Terminated"),
    
    /**
     * Session has expired.
     */
    EXPIRED("Expired"),
    
    /**
     * Session has encountered an error.
     */
    ERROR("Error"),
    
    /**
     * Session state is unknown.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for SessionState.
     * 
     * @param displayName the display name of the session state
     */
    SessionState(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the session state.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the session state by display name.
     * 
     * @param displayName the display name
     * @return the session state, or UNKNOWN if not found
     */
    public static SessionState fromDisplayName(String displayName) {
        for (SessionState state : values()) {
            if (state.displayName.equals(displayName)) {
                return state;
            }
        }
        return UNKNOWN;
    }
    
    /**
     * Checks if the session state is active.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this == ACTIVE || this == PROCESSING || this == WAITING_FOR_INPUT;
    }
    
    /**
     * Checks if the session state is terminated.
     * 
     * @return true if terminated, false otherwise
     */
    public boolean isTerminated() {
        return this == TERMINATED || this == EXPIRED || this == ERROR;
    }
}
