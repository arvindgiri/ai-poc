package com.arv.framework.adk.interfaces.tool;

/**
 * Represents a parameter for a tool.
 * This interface defines the structure of tool parameters in the ConnectADK framework.
 */
public interface IParameter {
    
    /**
     * Gets the name of the parameter.
     * 
     * @return the parameter name
     */
    String getName();
    
    /**
     * Gets the type of the parameter.
     * 
     * @return the parameter type
     */
    String getType();
    
    /**
     * Gets the description of the parameter.
     * 
     * @return the parameter description
     */
    String getDescription();
    
    /**
     * Checks if the parameter is required.
     * 
     * @return true if required, false otherwise
     */
    boolean isRequired();
    
    /**
     * Gets the default value of the parameter.
     * 
     * @return the default value
     */
    Object getDefaultValue();
    
    /**
     * Sets the default value of the parameter.
     * 
     * @param defaultValue the default value
     */
    void setDefaultValue(Object defaultValue);
    
    /**
     * Gets the validation rules for the parameter.
     * 
     * @return the validation rules
     */
    String getValidationRules();
    
    /**
     * Sets the validation rules for the parameter.
     * 
     * @param validationRules the validation rules
     */
    void setValidationRules(String validationRules);
    
    /**
     * Validates a parameter value.
     * 
     * @param value the value to validate
     * @return true if valid, false otherwise
     */
    boolean validateValue(Object value);
    
    /**
     * Gets the minimum value for numeric parameters.
     * 
     * @return the minimum value
     */
    Number getMinValue();
    
    /**
     * Sets the minimum value for numeric parameters.
     * 
     * @param minValue the minimum value
     */
    void setMinValue(Number minValue);
    
    /**
     * Gets the maximum value for numeric parameters.
     * 
     * @return the maximum value
     */
    Number getMaxValue();
    
    /**
     * Sets the maximum value for numeric parameters.
     * 
     * @param maxValue the maximum value
     */
    void setMaxValue(Number maxValue);
    
    /**
     * Gets the allowed values for the parameter.
     * 
     * @return the allowed values
     */
    java.util.List<Object> getAllowedValues();
    
    /**
     * Sets the allowed values for the parameter.
     * 
     * @param allowedValues the allowed values
     */
    void setAllowedValues(java.util.List<Object> allowedValues);
}
