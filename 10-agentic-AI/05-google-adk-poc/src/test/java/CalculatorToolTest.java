import com.arv.adk.extn.tool.function.CalculatorTool;
import com.arv.framework.adk.tools.ToolInput;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CalculatorTool functionality.
 * Tests various mathematical expressions and edge cases.
 */
public class CalculatorToolTest {
  
  private CalculatorTool calculator;
  private ToolInput input;
  
  @BeforeEach
  void setUp() {
    calculator = new CalculatorTool();
    input = new ToolInput("test_session");
  }
  
  @Test
  void testBasicArithmetic() {
    // Test addition
    input.setParameter("expression", "15 + 27");
    IToolResult result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("42", result.getData());
    
    // Test multiplication
    input.setParameter("expression", "100 * 5");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("500", result.getData());
    
    // Test division
    input.setParameter("expression", "100 / 4");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("25", result.getData());
    
    // Test subtraction
    input.setParameter("expression", "50 - 25");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("25", result.getData());
  }
  
  @Test
  void testComplexExpressions() {
    // Test parentheses
    input.setParameter("expression", "(10 + 5) * 3");
    IToolResult result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("45", result.getData());
    
    // Test order of operations
    input.setParameter("expression", "2 + 3 * 4");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("14", result.getData());
    
    // Test decimal calculations
    input.setParameter("expression", "3.5 * 2");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("7", result.getData());
  }
  
  @Test
  void testNaturalLanguageQueries() {
    // Test "What's" queries
    input.setParameter("expression", "What's 15 + 27?");
    IToolResult result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("42", result.getData());
    
    // Test "Calculate" queries
    input.setParameter("expression", "Calculate 100 * 5");
    result = calculator.execute(input);
    assertTrue(result.isSuccess());
    assertEquals("500", result.getData());
  }
  
  @Test
  void testErrorCases() {
    // Test division by zero
    input.setParameter("expression", "10 / 0");
    IToolResult result = calculator.execute(input);
    assertFalse(result.isSuccess());
    assertTrue(result.getError().contains("Division by zero"));
    
    // Test invalid expression
    input.setParameter("expression", "abc + def");
    result = calculator.execute(input);
    assertFalse(result.isSuccess());
    assertTrue(result.hasError());
    
    // Test empty expression
    input.setParameter("expression", "");
    result = calculator.execute(input);
    assertFalse(result.isSuccess());
    assertEquals("Expression cannot be empty", result.getError());
    
    // Test math help without expression
    input.setParameter("expression", "Help me with math");
    result = calculator.execute(input);
    assertFalse(result.isSuccess());
    assertEquals("Please provide a specific mathematical expression to calculate", result.getError());
  }
  
  @Test
  void testToolMetadata() {
    assertEquals("calculator", calculator.getId());
    assertEquals("Calculator", calculator.getName());
    assertTrue(calculator.getDescription().contains("mathematical expressions"));
    assertTrue(calculator.isEnabled());
    assertNotNull(calculator.getMetadata());
  }
  
  @Test
  void testInputValidation() {
    // Set up valid input first
    input.setParameter("expression", "2 + 2");
    
    // Test valid input
    assertTrue(calculator.validateInput(input));
    
    // Test null input
    assertFalse(calculator.validateInput(null));
    
    // Test input without expression parameter
    ToolInput emptyInput = new ToolInput("test_session");
    assertFalse(calculator.validateInput(emptyInput));
  }
}
