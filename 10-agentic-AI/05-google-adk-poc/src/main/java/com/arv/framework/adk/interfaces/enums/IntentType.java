package com.arv.framework.adk.interfaces.enums;

/**
 * Types of user intents (WEATHER_QUERY, TIME_QUERY, etc.).
 * This enum defines the different types of user intents that can be identified in the ConnectADK framework.
 */
public enum IntentType {
    
    /**
     * Weather query intent.
     */
    WEATHER_QUERY("Weather Query"),
    
    /**
     * Time query intent.
     */
    TIME_QUERY("Time Query"),
    
    /**
     * Information request intent.
     */
    INFORMATION_REQUEST("Information Request"),
    
    /**
     * Command execution intent.
     */
    COMMAND_EXECUTION("Command Execution"),
    
    /**
     * Tool selection intent.
     */
    TOOL_SELECTION("Tool Selection"),
    
    /**
     * Parameter extraction intent.
     */
    PARAMETER_EXTRACTION("Parameter Extraction"),
    
    /**
     * Confirmation intent.
     */
    CONFIRMATION("Confirmation"),
    
    /**
     * Cancellation intent.
     */
    CANCELLATION("Cancellation"),
    
    /**
     * Help request intent.
     */
    HELP_REQUEST("Help Request"),
    
    /**
     * Status inquiry intent.
     */
    STATUS_INQUIRY("Status Inquiry"),
    
    /**
     * Data processing intent.
     */
    DATA_PROCESSING("Data Processing"),
    
    /**
     * File operation intent.
     */
    FILE_OPERATION("File Operation"),
    
    /**
     * API call intent.
     */
    API_CALL("API Call"),
    
    /**
     * Database query intent.
     */
    DATABASE_QUERY("Database Query"),
    
    /**
     * Custom intent.
     */
    CUSTOM("Custom"),
    
    /**
     * Unknown intent type.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for IntentType.
     * 
     * @param displayName the display name of the intent type
     */
    IntentType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the intent type.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the intent type by display name.
     * 
     * @param displayName the display name
     * @return the intent type, or UNKNOWN if not found
     */
    public static IntentType fromDisplayName(String displayName) {
        for (IntentType intentType : values()) {
            if (intentType.displayName.equals(displayName)) {
                return intentType;
            }
        }
        return UNKNOWN;
    }
}
