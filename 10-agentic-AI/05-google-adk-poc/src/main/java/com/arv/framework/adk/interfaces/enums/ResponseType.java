package com.arv.framework.adk.interfaces.enums;

/**
 * Types of parsed responses (TOOL_SELECTION, PARAMETER_EXTRACTION, etc.).
 * This enum defines the different types of responses that can be parsed in the ConnectADK framework.
 */
public enum ResponseType {
    
    /**
     * Tool selection response.
     */
    TOOL_SELECTION("Tool Selection"),
    
    /**
     * Parameter extraction response.
     */
    PARAMETER_EXTRACTION("Parameter Extraction"),
    
    /**
     * Intent analysis response.
     */
    INTENT_ANALYSIS("Intent Analysis"),
    
    /**
     * Confirmation response.
     */
    CONFIRMATION("Confirmation"),
    
    /**
     * Error response.
     */
    ERROR("Error"),
    
    /**
     * Success response.
     */
    SUCCESS("Success"),
    
    /**
     * Information response.
     */
    INFORMATION("Information"),
    
    /**
     * Warning response.
     */
    WARNING("Warning"),
    
    /**
     * Question response.
     */
    QUESTION("Question"),
    
    /**
     * Command response.
     */
    COMMAND("Command"),
    
    /**
     * Status response.
     */
    STATUS("Status"),
    
    /**
     * Data response.
     */
    DATA("Data"),
    
    /**
     * Unknown response type.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for ResponseType.
     * 
     * @param displayName the display name of the response type
     */
    ResponseType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the response type.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the response type by display name.
     * 
     * @param displayName the display name
     * @return the response type, or UNKNOWN if not found
     */
    public static ResponseType fromDisplayName(String displayName) {
        for (ResponseType responseType : values()) {
            if (responseType.displayName.equals(displayName)) {
                return responseType;
            }
        }
        return UNKNOWN;
    }
}
