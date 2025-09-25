package com.arv.adk.extn.tool.function;

import com.arv.framework.adk.interfaces.enums.ReturnType;
import com.arv.framework.adk.interfaces.enums.ToolCategory;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.tools.ToolMetadata;
import com.arv.framework.adk.tools.Parameter;
import com.arv.framework.adk.tools.ToolResult;
import com.udojava.evalex.Expression;
import java.math.BigDecimal;

/**
 * Calculator tool implementation using EvalEx library.
 * Evaluates mathematical expressions and returns results.
 */
public class CalculatorTool implements ITool {
  
  private static final String TOOL_ID = "calculator";
  private static final String TOOL_NAME = "Calculator";
  private static final String TOOL_DESCRIPTION = 
      "Evaluates mathematical expressions and returns the result. "
      + "Supports basic arithmetic (+, -, *, /), parentheses, and common mathematical functions.";
  
  private boolean enabled;
  private IToolMetadata metadata;
  
  /**
   * Constructor for CalculatorTool.
   */
  public CalculatorTool() {
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
        return ToolResult.failure("Invalid input: expression parameter is required")
            .withExecutionTime(startTime);
      }
      
      // Get expression from input
      String expression = getExpressionFromInput(input);
      if (expression == null || expression.trim().isEmpty()) {
        return ToolResult.failure("Expression cannot be empty")
            .withExecutionTime(startTime);
      }
      
      // Clean and validate expression
      String cleanExpression = cleanExpression(expression);
      
      // Evaluate expression using EvalEx
      Expression expr = new Expression(cleanExpression);
      BigDecimal result = expr.eval();
      
      // Format result for display
      String resultString = formatResult(result);
      
      return ToolResult.success(resultString)
          .withExecutionTime(startTime)
          .withMetadata("original_expression", expression)
          .withMetadata("clean_expression", cleanExpression)
          .withMetadata("result_type", "BigDecimal")
          .withMetadata("precision", result.precision());
      
    } catch (IllegalArgumentException e) {
      return ToolResult.failure(e.getMessage())
          .withExecutionTime(startTime);
    } catch (Expression.ExpressionException e) {
      return ToolResult.failure("Invalid mathematical expression: " + e.getMessage())
          .withExecutionTime(startTime);
    } catch (ArithmeticException e) {
      return ToolResult.failure("Arithmetic error: " + e.getMessage())
          .withExecutionTime(startTime);
    } catch (Exception e) {
      return ToolResult.failure("Calculation error: " + e.getMessage())
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
    
    // Check if expression parameter exists
    return input.hasParameter("expression") || input.hasParameter("query");
  }
  
  /**
   * Extracts mathematical expression from tool input.
   * 
   * @param input the tool input
   * @return the mathematical expression
   */
  private String getExpressionFromInput(IToolInput input) {
    // Try "expression" parameter first
    Object expressionObj = input.getParameter("expression");
    if (expressionObj != null) {
      return expressionObj.toString();
    }
    
    // Try "query" parameter as fallback
    Object queryObj = input.getParameter("query");
    if (queryObj != null) {
      return queryObj.toString();
    }
    
    return null;
  }
  
  /**
   * Cleans the mathematical expression for evaluation.
   * 
   * @param expression the raw expression
   * @return the cleaned expression
   */
  private String cleanExpression(String expression) {
    // Remove common prefixes that might come from natural language
    String cleaned = expression.trim();
    
    // Remove apostrophes (like in "What's")
    cleaned = cleaned.replaceAll("'", "");
    
    // Remove question words and common prefixes
    cleaned = cleaned.replaceAll("(?i)^(whats?\\s+|what\\s+is\\s+|calculate\\s+|compute\\s+|solve\\s+|help\\s+me\\s+with\\s+)", "");
    cleaned = cleaned.replaceAll("(?i)(\\?|=\\s*\\?)$", ""); // Remove trailing ? or = ?
    
    // Replace common words with operators
    cleaned = cleaned.replaceAll("(?i)\\s+(plus|and)\\s+", " + ");
    cleaned = cleaned.replaceAll("(?i)\\s+minus\\s+", " - ");
    cleaned = cleaned.replaceAll("(?i)\\s+(times|multiplied\\s+by)\\s+", " * ");
    cleaned = cleaned.replaceAll("(?i)\\s+(divided\\s+by|over)\\s+", " / ");
    
    // Handle special cases for math help
    if (cleaned.toLowerCase().contains("math") && !cleaned.matches(".*\\d.*")) {
      throw new IllegalArgumentException("Please provide a specific mathematical expression to calculate");
    }
    
    // Replace text numbers with digits (basic ones)
    cleaned = cleaned.replaceAll("(?i)\\bone\\b", "1");
    cleaned = cleaned.replaceAll("(?i)\\btwo\\b", "2");
    cleaned = cleaned.replaceAll("(?i)\\bthree\\b", "3");
    cleaned = cleaned.replaceAll("(?i)\\bfour\\b", "4");
    cleaned = cleaned.replaceAll("(?i)\\bfive\\b", "5");
    cleaned = cleaned.replaceAll("(?i)\\bsix\\b", "6");
    cleaned = cleaned.replaceAll("(?i)\\bseven\\b", "7");
    cleaned = cleaned.replaceAll("(?i)\\beight\\b", "8");
    cleaned = cleaned.replaceAll("(?i)\\bnine\\b", "9");
    cleaned = cleaned.replaceAll("(?i)\\bten\\b", "10");
    
    return cleaned.trim();
  }
  
  /**
   * Formats the BigDecimal result for display.
   * 
   * @param result the calculation result
   * @return formatted result string
   */
  private String formatResult(BigDecimal result) {
    // Remove trailing zeros and decimal point if not needed
    String resultString = result.stripTrailingZeros().toPlainString();
    
    // Add thousand separators for large numbers (optional)
    if (result.abs().compareTo(new BigDecimal("1000")) >= 0 
        && !resultString.contains(".")) {
      try {
        long longValue = result.longValueExact();
        resultString = String.format("%,d", longValue);
      } catch (ArithmeticException e) {
        // Keep original string if conversion fails
      }
    }
    
    return resultString;
  }
  
  /**
   * Creates metadata for the calculator tool.
   * 
   * @return the tool metadata
   */
  private IToolMetadata createMetadata() {
    ToolMetadata meta = new ToolMetadata(TOOL_ID, TOOL_NAME, TOOL_DESCRIPTION);
    meta.setCategory(ToolCategory.UTILITY);
    meta.setReturnType(ReturnType.STRING);
    meta.setVersion("1.0.0");
    
    // Add expression parameter
    Parameter expressionParam = new Parameter(
        "expression", 
        "string", 
        "Mathematical expression to evaluate (e.g., '2 + 3 * 4', '(10 + 5) / 3')", 
        true
    );
    meta.addParameter(expressionParam);
    
    // Add optional query parameter as alternative
    Parameter queryParam = new Parameter(
        "query", 
        "string", 
        "Natural language mathematical query (e.g., 'What is 15 plus 27?')", 
        false
    );
    meta.addParameter(queryParam);
    
    // Add tool properties
    meta.setProperty("supports_functions", true);
    meta.setProperty("precision", "arbitrary");
    meta.setProperty("max_expression_length", 1000);
    
    return meta;
  }
}
