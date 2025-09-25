import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.arv.adk.tools.MultiplyTool;
import com.arv.adk.tools.SumTool;
import com.arv.adk.tools.WikipediaSearchTool;
import com.arv.adk.tools.GoogleSearchTool;
import com.arv.framework.adk.impl.ToolExecutionEngine;
import com.arv.framework.adk.interfaces.tool.IToolExecutionEngine;
import com.arv.framework.adk.impl.ConnectAgent;
import com.arv.framework.adk.interfaces.core.IAgent;
import com.arv.framework.adk.impl.GeminiLlmConfig;
import com.arv.framework.adk.impl.GeminiLlmService;
import com.arv.framework.adk.impl.SimpleSessionManager;
import com.arv.framework.adk.impl.SimpleToolRegistry;
import com.arv.framework.adk.interfaces.core.ISessionManager;
import com.arv.framework.adk.interfaces.core.IToolRegistry;
import com.arv.framework.adk.interfaces.gemini.ILlmConfig;
import com.arv.framework.adk.interfaces.gemini.ILlmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for Agent processMessage method using real ConnectADK framework. This test uses actual
 * Gemini LLM service and ConnectADK components.
 */
class AgentTest {

  private IAgent agent;
  private ILlmService llmService;
  private IToolRegistry toolRegistry;
  private ISessionManager sessionManager;
  private IToolExecutionEngine toolExecutionEngine;

  @BeforeEach
  void setUp() {
    // Create LLM configuration with API key
    ILlmConfig llmConfig = new GeminiLlmConfig();
    llmConfig.setApiKey(
        "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U"); // Using the API key from Main.java
    llmConfig.setModel("gemini-2.0-flash");

    // Create LLM service
    llmService = new GeminiLlmService(llmConfig);

    // Create tool registry and register tools
    toolRegistry = new SimpleToolRegistry();
    toolRegistry.registerTool(new MultiplyTool());
    toolRegistry.registerTool(new SumTool());
    toolRegistry.registerTool(new WikipediaSearchTool());
    toolRegistry.registerTool(new GoogleSearchTool());

    // Create tool execution engine
    toolExecutionEngine = new ToolExecutionEngine(toolRegistry);

    // Create session manager
    sessionManager = new SimpleSessionManager();

    // Create the agent with all components
    agent = new ConnectAgent(llmService, toolExecutionEngine, sessionManager);
  }

  @Test
  void testProcessMessageMultiply3With4() {
    // Test the specific case: "multiply 3 with 4" should return "12"
    String input = "multiply 3 with 4";
    String sessionId = "test_session";

    Object result = agent.processMessage(input, sessionId);

    // Convert result to string for comparison
    String resultStr = result != null ? result.toString() : "null";

    assertEquals("12", resultStr, "Agent should return '12' for 'multiply 3 with 4'");
  }

  @Test
  void testProcessMessageMultiply5With6() {
    // Test another multiplication case
    String input = "multiply 5 with 6";
    String sessionId = "test_session";

    Object result = agent.processMessage(input, sessionId);
    String resultStr = result != null ? result.toString() : "null";

    assertEquals("30", resultStr, "Agent should return '30' for 'multiply 5 with 6'");
  }

  @Test
  void testProcessMessageAdd7And8() {
    // Test addition case
    String input = "add 7 and 8";
    String sessionId = "test_session";

    Object result = agent.processMessage(input, sessionId);
    String resultStr = result != null ? result.toString() : "null";

    assertEquals("15", resultStr, "Agent should return '15' for 'add 7 and 8'");
  }

  @Test
  void testProcessMessageUnknownQuery() {
    // Test unknown query - should use LLM service
    String input = "what is the weather today?";
    String sessionId = "test_session";

    Object result = agent.processMessage(input, sessionId);
    String resultStr = result != null ? result.toString() : "null";

    // Should get a response from LLM service (not an error message)
    assertNotNull(result, "Agent should return a response");
    assertFalse(resultStr.contains("I don't understand"),
        "Agent should use LLM service for unknown queries, not return error message");
  }

  @Test
  void testCalculateTwiceIndiaPopulation() {
    // Test calculating twice the population of India
    // Expected: Gemini should search Wikipedia for India's population, then multiply by 2
    // India's population is approximately 1.4 billion, so twice would be ~2.8 billion
    String input = "calculate twice the population of india in million";
    String sessionId = "test_session";

    Object result = agent.processMessage(input, sessionId);
    assertTrue(result instanceof Long, "Population should be a number");
    assertTrue((Long) result > 1000_000_000 && (Long) result > 2000_000_000,
        "Population should be between 1-2 billion");
  }

  @Test
  void testToolRegistration() {
    // Test that tools are properly registered
    assertEquals(4, toolRegistry.getToolCount(), "Should have 4 tools registered");

    // Test specific tools exist by ID
    assertTrue(toolRegistry.getTool("multiply").isPresent(), "Multiply tool should be registered");
    assertTrue(toolRegistry.getTool("sum").isPresent(), "Sum tool should be registered");
    assertTrue(toolRegistry.getTool("wikipedia_search").isPresent(),
        "Wikipedia search tool should be registered");
    assertTrue(toolRegistry.getTool("google_search").isPresent(),
        "Google search tool should be registered");
  }

  @Test
  void testLlmServiceConfiguration() {
    // Test that LLM service is properly configured
    assertEquals("gemini-2.0-flash", llmService.getModel(), "Should use correct model");
    assertNotNull(llmService.getConfig(), "LLM service should have config");
    assertNotNull(llmService.getConfig().getApiKey(), "LLM service should have API key");
  }
}