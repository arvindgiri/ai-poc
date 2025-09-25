package com.arv.adk.tools;

import com.arv.framework.adk.interfaces.enums.ReturnType;
import com.arv.framework.adk.interfaces.enums.ToolCategory;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.tools.ToolMetadata;
import com.arv.framework.adk.tools.Parameter;
import com.arv.framework.adk.tools.ToolResult;

/**
 * Sum tool implementation for adding two integers.
 * Handles addition operations with proper validation and error handling.
 */
public class SumTool implements ITool {
  
  private static final String TOOL_ID = "sum";
  private static final String TOOL_NAME = "Sum";
  private static final String TOOL_DESCRIPTION = 
      "Add two integers and return the result. "
      + "Supports natural language queries like 'add 3 and 5' or 'what is 4 plus 6'.";
  
  private boolean enabled;
  private IToolMetadata metadata;
  
  /**
   * Constructor for SumTool.
   */
  public SumTool() {
    this.enabled = true;
    this.metadata = createMetadata();
  }
  
  @Override
  public String getId() {
    return TOOL_ID;
  }
  
  @Override
  public String getName() {
    return TOOL_NAME;
  }
  
  @Override
  public String getDescription() {
    return TOOL_DESCRIPTION;
  }
  
  @Override
  public IToolResult execute(IToolInput input) {
    long startTime = System.currentTimeMillis();
    
    try {
      // Validate input
      if (!validateInput(input)) {
        return ToolResult.failure("Invalid input: a and b parameters are required")
            .withExecutionTime(startTime);
      }
      
      // Extract parameters
      Integer a = extractIntegerParameter(input, "a");
      Integer b = extractIntegerParameter(input, "b");
      
      if (a == null || b == null) {
        return ToolResult.failure("Both a and b must be valid integers")
            .withExecutionTime(startTime);
      }
      
      // Perform addition
      long result = (long) a + b;
      
      // Check for overflow
      if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        return ToolResult.failure("Result overflow: " + result + " is outside integer range")
            .withExecutionTime(startTime);
      }
      
      String resultString = String.valueOf((int) result);
      
      return ToolResult.success(resultString)
          .withExecutionTime(startTime)
          .withMetadata("a", a)
          .withMetadata("b", b)
          .withMetadata("operation", "addition")
          .withMetadata("result", (int) result);
      
    } catch (NumberFormatException e) {
      return ToolResult.failure("Invalid number format: " + e.getMessage())
          .withExecutionTime(startTime);
    } catch (Exception e) {
      return ToolResult.failure("Addition error: " + e.getMessage())
          .withExecutionTime(startTime);
    }
  }
  
  @Override
  public boolean isEnabled() {
    return enabled;
  }
  
  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  @Override
  public IToolMetadata getMetadata() {
    return metadata;
  }
  
  @Override
  public boolean validateInput(IToolInput input) {
    if (input == null) {
      return false;
    }
    
    // Check if both a and b parameters exist
    return input.hasParameter("a") && input.hasParameter("b");
  }
  
  /**
   * Extracts integer parameter from tool input.
   * 
   * @param input the tool input
   * @param paramName the parameter name
   * @return the integer value or null if invalid
   */
  private Integer extractIntegerParameter(IToolInput input, String paramName) {
    Object value = input.getParameter(paramName);
    
    if (value == null) {
      return null;
    }
    
    if (value instanceof Integer) {
      return (Integer) value;
    }
    
    if (value instanceof Number) {
      return ((Number) value).intValue();
    }
    
    if (value instanceof String) {
      try {
        return Integer.parseInt(((String) value).trim());
      } catch (NumberFormatException e) {
        return null;
      }
    }
    
    return null;
  }
  
  /**
   * Creates metadata for the sum tool.
   * 
   * @return the tool metadata
   */
  private IToolMetadata createMetadata() {
    ToolMetadata meta = new ToolMetadata(TOOL_ID, TOOL_NAME, TOOL_DESCRIPTION);
    meta.setCategory(ToolCategory.UTILITY);
    meta.setReturnType(ReturnType.INTEGER);
    meta.setVersion("1.0.0");
    
    // Add a parameter
    Parameter aParam = new Parameter(
        "a", 
        "integer", 
        "First integer to add", 
        true
    );
    meta.addParameter(aParam);
    
    // Add b parameter
    Parameter bParam = new Parameter(
        "b", 
        "integer", 
        "Second integer to add", 
        true
    );
    meta.addParameter(bParam);
    
    // Add tool properties
    meta.setProperty("supports_overflow_detection", true);
    meta.setProperty("max_safe_value", Integer.MAX_VALUE);
    meta.setProperty("min_safe_value", Integer.MIN_VALUE);
    
    return meta;
  }
}
