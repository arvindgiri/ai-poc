package com.arv.adk.interfaces.enums;

/**
 * Context for parsing operations (TOOL_SELECTION, PARAMETER_EXTRACTION, etc.).
 * This enum defines the different contexts in which parsing operations can occur in the ConnectADK framework.
 */
public enum ParseContext {
    
    /**
     * Tool selection context.
     */
    TOOL_SELECTION("Tool Selection"),
    
    /**
     * Parameter extraction context.
     */
    PARAMETER_EXTRACTION("Parameter Extraction"),
    
    /**
     * Intent analysis context.
     */
    INTENT_ANALYSIS("Intent Analysis"),
    
    /**
     * Response parsing context.
     */
    RESPONSE_PARSING("Response Parsing"),
    
    /**
     * Message processing context.
     */
    MESSAGE_PROCESSING("Message Processing"),
    
    /**
     * Validation context.
     */
    VALIDATION("Validation"),
    
    /**
     * Error handling context.
     */
    ERROR_HANDLING("Error Handling"),
    
    /**
     * Configuration context.
     */
    CONFIGURATION("Configuration"),
    
    /**
     * Session management context.
     */
    SESSION_MANAGEMENT("Session Management"),
    
    /**
     * Tool execution context.
     */
    TOOL_EXECUTION("Tool Execution"),
    
    /**
     * Unknown parsing context.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for ParseContext.
     * 
     * @param displayName the display name of the parsing context
     */
    ParseContext(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the parsing context.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the parsing context by display name.
     * 
     * @param displayName the display name
     * @return the parsing context, or UNKNOWN if not found
     */
    public static ParseContext fromDisplayName(String displayName) {
        for (ParseContext context : values()) {
            if (context.displayName.equals(displayName)) {
                return context;
            }
        }
        return UNKNOWN;
    }
}
