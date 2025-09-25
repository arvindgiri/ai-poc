package com.arv.framework.adk.interfaces.enums;

/**
 * Categories for organizing tools.
 * This enum defines the different categories that tools can belong to in the ConnectADK framework.
 */
public enum ToolCategory {
    
    /**
     * Weather-related tools.
     */
    WEATHER("Weather"),
    
    /**
     * Time-related tools.
     */
    TIME("Time"),
    
    /**
     * Communication tools.
     */
    COMMUNICATION("Communication"),
    
    /**
     * Data processing tools.
     */
    DATA_PROCESSING("Data Processing"),
    
    /**
     * File operation tools.
     */
    FILE_OPERATIONS("File Operations"),
    
    /**
     * Database tools.
     */
    DATABASE("Database"),
    
    /**
     * API integration tools.
     */
    API_INTEGRATION("API Integration"),
    
    /**
     * Utility tools.
     */
    UTILITY("Utility"),
    
    /**
     * Custom tools.
     */
    CUSTOM("Custom"),
    
    /**
     * Unknown category.
     */
    UNKNOWN("Unknown");
    
    private final String displayName;
    
    /**
     * Constructor for ToolCategory.
     * 
     * @param displayName the display name of the category
     */
    ToolCategory(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of the category.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the category by display name.
     * 
     * @param displayName the display name
     * @return the category, or UNKNOWN if not found
     */
    public static ToolCategory fromDisplayName(String displayName) {
        for (ToolCategory category : values()) {
            if (category.displayName.equals(displayName)) {
                return category;
            }
        }
        return UNKNOWN;
    }
}
