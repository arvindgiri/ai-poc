package com.arv.framework.adk.tools;

import com.arv.framework.adk.interfaces.tool.IParameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IParameter for tool parameters.
 * Represents a parameter with validation rules and constraints.
 */
public class Parameter implements IParameter {
  
  private String name;
  private String type;
  private String description;
  private boolean required;
  private Object defaultValue;
  private String validationRules;
  private Number minValue;
  private Number maxValue;
  private List<Object> allowedValues;
  
  /**
   * Constructor for Parameter.
   * 
   * @param name the parameter name
   * @param type the parameter type
   * @param description the parameter description
   * @param required whether the parameter is required
   */
  public Parameter(String name, String type, String description, boolean required) {
    this.name = name;
    this.type = type;
    this.description = description;
    this.required = required;
    this.allowedValues = new ArrayList<>();
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public String getType() {
    return type;
  }
  
  @Override
  public String getDescription() {
    return description;
  }
  
  @Override
  public boolean isRequired() {
    return required;
  }
  
  @Override
  public Object getDefaultValue() {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  @Override
  public String getValidationRules() {
    return validationRules;
  }
  
  @Override
  public void setValidationRules(String validationRules) {
    this.validationRules = validationRules;
  }
  
  @Override
  public boolean validateValue(Object value) {
    // Basic validation logic
    if (required && value == null) {
      return false;
    }
    
    if (value == null) {
      return true; // Optional parameter with null value is valid
    }
    
    // Type validation
    if (!isValidType(value)) {
      return false;
    }
    
    // Range validation for numeric types
    if (value instanceof Number && !isInRange((Number) value)) {
      return false;
    }
    
    // Allowed values validation
    if (!allowedValues.isEmpty() && !allowedValues.contains(value)) {
      return false;
    }
    
    return true;
  }
  
  @Override
  public Number getMinValue() {
    return minValue;
  }
  
  @Override
  public void setMinValue(Number minValue) {
    this.minValue = minValue;
  }
  
  @Override
  public Number getMaxValue() {
    return maxValue;
  }
  
  @Override
  public void setMaxValue(Number maxValue) {
    this.maxValue = maxValue;
  }
  
  @Override
  public List<Object> getAllowedValues() {
    return new ArrayList<>(allowedValues);
  }
  
  @Override
  public void setAllowedValues(List<Object> allowedValues) {
    this.allowedValues = new ArrayList<>(allowedValues);
  }
  
  /**
   * Validates if the value matches the expected type.
   * 
   * @param value the value to validate
   * @return true if type is valid, false otherwise
   */
  private boolean isValidType(Object value) {
    switch (type.toLowerCase()) {
      case "string":
        return value instanceof String;
      case "integer":
      case "int":
        return value instanceof Integer;
      case "double":
      case "number":
        return value instanceof Number;
      case "boolean":
        return value instanceof Boolean;
      default:
        return true; // Unknown type, assume valid
    }
  }
  
  /**
   * Validates if the numeric value is within the specified range.
   * 
   * @param value the numeric value to validate
   * @return true if within range, false otherwise
   */
  private boolean isInRange(Number value) {
    double doubleValue = value.doubleValue();
    
    if (minValue != null && doubleValue < minValue.doubleValue()) {
      return false;
    }
    
    if (maxValue != null && doubleValue > maxValue.doubleValue()) {
      return false;
    }
    
    return true;
  }
}
