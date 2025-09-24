package com.arv.adk.interfaces.enums;

/**
 * Types of return values from tools.
 * This enum defines the different types of return values that tools can produce in the ConnectADK framework.
 */
public enum ReturnType {
    
    /**
     * String return type.
     */
    STRING("String"),
    
    /**
     * Integer return type.
     */
    INTEGER("Integer"),
    
    /**
     * Double return type.
     */
    DOUBLE("Double"),
    
    /**
     * Boolean return type.
     */
    BOOLEAN("Boolean"),
    
    /**
     * JSON object return type.
     */
    JSON_OBJECT("JSON Object"),
    
    /**
     * JSON array return type.
     */
    JSON_ARRAY("JSON Array"),
    
    /**
     * List return type.
     */
    LIST("List"),
    
    /**
     * Map return type.
     */
    MAP("Map"),
    
    /**
     * File return type.
     */
    FILE("File"),
    
    /**
     * Binary data return type.
     */
    BINARY("Binary"),
    
    /**
     * Custom object return type.
     */
    CUSTOM_OBJECT("Custom Object"),
    
    /**
     * Void return type.
     */
    VOID("Void"),
    
    /**
     * Unknown return type.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for ReturnType.
     * 
     * @param displayName the display name of the return type
     */
    ReturnType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the return type.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the return type by display name.
     * 
     * @param displayName the display name
     * @return the return type, or UNKNOWN if not found
     */
    public static ReturnType fromDisplayName(String displayName) {
        for (ReturnType returnType : values()) {
            if (returnType.displayName.equals(displayName)) {
                return returnType;
            }
        }
        return UNKNOWN;
    }
}
