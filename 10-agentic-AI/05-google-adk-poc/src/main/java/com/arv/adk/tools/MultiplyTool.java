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
 * Multiply tool implementation for multiplying two integers.
 * Handles multiplication operations with proper validation and error handling.
 */
public class MultiplyTool implements ITool {
  
  private static final String TOOL_ID = "multiply";
  private static final String TOOL_NAME = "Multiply";
  private static final String TOOL_DESCRIPTION = 
      "Multiply two integers and return the result. "
      + "Supports natural language queries like 'multiply 3 with 5' or 'what is 4 times 6'.";
  
  private boolean enabled;
  private IToolMetadata metadata;
  
  /**
   * Constructor for MultiplyTool.
   */
  public MultiplyTool() {
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
        return ToolResult.failure("Invalid input: x and y parameters are required")
            .withExecutionTime(startTime);
      }
      
      // Extract parameters
      Long x = extractLongParameter(input, "x");
      Long y = extractLongParameter(input, "y");
      
      if (x == null || y == null) {
        return ToolResult.failure("Both x and y must be valid numbers")
            .withExecutionTime(startTime);
      }
      
      // Perform multiplication
      long result = x * y;
      
      // Check for long overflow (much larger range)
      if (x != 0 && result / x != y) {
        return ToolResult.failure("Result overflow: multiplication result is too large")
            .withExecutionTime(startTime);
      }
      
      String resultString = String.valueOf(result);
      
      return ToolResult.success(resultString)
          .withExecutionTime(startTime)
          .withMetadata("x", x)
          .withMetadata("y", y)
          .withMetadata("operation", "multiplication")
          .withMetadata("result", result);
      
    } catch (NumberFormatException e) {
      return ToolResult.failure("Invalid number format: " + e.getMessage())
          .withExecutionTime(startTime);
    } catch (Exception e) {
      return ToolResult.failure("Multiplication error: " + e.getMessage())
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
    
    // Check if both x and y parameters exist
    return input.hasParameter("x") && input.hasParameter("y");
  }
  
  /**
   * Extracts long parameter from tool input.
   * 
   * @param input the tool input
   * @param paramName the parameter name
   * @return the long value or null if invalid
   */
  private Long extractLongParameter(IToolInput input, String paramName) {
    Object value = input.getParameter(paramName);
    
    if (value == null) {
      return null;
    }
    
    if (value instanceof Long) {
      return (Long) value;
    }
    
    if (value instanceof Number) {
      return ((Number) value).longValue();
    }
    
    if (value instanceof String) {
      try {
        return Long.parseLong(((String) value).trim());
      } catch (NumberFormatException e) {
        return null;
      }
    }
    
    return null;
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
   * Creates metadata for the multiply tool.
   * 
   * @return the tool metadata
   */
  private IToolMetadata createMetadata() {
    ToolMetadata meta = new ToolMetadata(TOOL_ID, TOOL_NAME, TOOL_DESCRIPTION);
    meta.setCategory(ToolCategory.UTILITY);
    meta.setReturnType(ReturnType.INTEGER);
    meta.setVersion("1.0.0");
    
    // Add x parameter
    Parameter xParam = new Parameter(
        "x", 
        "integer", 
        "First integer to multiply", 
        true
    );
    meta.addParameter(xParam);
    
    // Add y parameter
    Parameter yParam = new Parameter(
        "y", 
        "integer", 
        "Second integer to multiply", 
        true
    );
    meta.addParameter(yParam);
    
    // Add tool properties
    meta.setProperty("supports_overflow_detection", true);
    meta.setProperty("max_safe_value", Integer.MAX_VALUE);
    meta.setProperty("min_safe_value", Integer.MIN_VALUE);
    
    return meta;
  }
}
