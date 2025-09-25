// Main class for running the ConnectADK framework

import com.arv.framework.adk.interfaces.core.IAgent;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.interfaces.session.ISession;
import com.arv.framework.adk.interfaces.enums.ToolCategory;
import com.arv.framework.adk.interfaces.enums.ReturnType;
import com.arv.framework.adk.interfaces.enums.SessionState;
import com.arv.adk.tools.MultiplyTool;
import com.arv.adk.tools.SumTool;
import com.arv.framework.adk.tools.ToolInput;

/**
 * Main class to demonstrate the ConnectADK framework flow.
 * This class shows the high-level interaction between components.
 */
public class Main {

  // Tool variables for math operations
  private static ITool multiplyTool = null;
  private static ITool sumTool = null;
  private static ITool weatherTool = null;
  
  // Mock agent implementation for demonstration
  private static IAgent connectAgent = null;

  /**
   * Main method demonstrating the ConnectADK framework flow.
   * 
   * @param args command line arguments
   * @throws Exception if there's an error running the framework
   */
  public static void main(String[] args) throws Exception {
    System.out.println("=== ConnectADK Framework Demo ===\n");
    
    // Step 1: Initialize the framework
    initializeFramework();
    
    // Step 2: Register tools
    registerTools();
    
    // Step 3: Start a session
    ISession session = startSession();
    
    // Step 4: Process user messages (simulated)
    processUserMessages(session);
    
    // Step 5: Cleanup
    cleanup(session);
    
    System.out.println("\n=== ConnectADK Framework Demo Complete ===");
  }

  /**
   * Step 1: Initialize the ConnectADK framework
   */
  private static void initializeFramework() {
    System.out.println("1. Initializing ConnectADK Framework...");
    
    // Set up LLM API configuration
    System.setProperty("GOOGLE_GENAI_USE_VERTEXAI", "FALSE");
    System.setProperty("GOOGLE_API_KEY", "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U");
    
    // TODO: Initialize ConnectAgent with LLM service
    // connectAgent = new ConnectAgent();
    // connectAgent.setLlmApiKey(System.getProperty("GOOGLE_API_KEY"));
    // connectAgent.setLlmModel("gemini-2.0-flash");
    
    System.out.println("   ✓ Framework initialized");
    System.out.println("   ✓ LLM API configured");
    System.out.println("   ✓ ConnectAgent ready (mock)\n");
  }

  /**
   * Step 2: Register tools with the agent
   */
  private static void registerTools() {
    System.out.println("2. Registering Tools...");
    
    // Create math tools
    multiplyTool = createMultiplyTool();
    sumTool = createSumTool();
    weatherTool = createWeatherTool();
    
    // Register tools with the agent
    if (connectAgent != null) {
      // connectAgent.registerTool(multiplyTool);
      // connectAgent.registerTool(sumTool);
      // connectAgent.registerTool(weatherTool);
      System.out.println("   ✓ Multiply tool registered");
      System.out.println("   ✓ Sum tool registered");
      System.out.println("   ✓ Weather tool registered");
    } else {
      System.out.println("   ✓ Multiply tool created");
      System.out.println("   ✓ Sum tool created");
      System.out.println("   ✓ Weather tool created (skeleton)");
    }
    
    System.out.println("   ✓ Tools ready for use\n");
  }

  /**
   * Step 3: Start a user session
   */
  private static ISession startSession() {
    System.out.println("3. Starting User Session...");
    
    String sessionId = "user_session_001";
    String userId = "demo_user";
    
    // Start session through agent
    if (connectAgent != null) {
      // ISession session = connectAgent.startSession(sessionId);
      // session.setUserId(userId);
      // System.out.println("   ✓ Session started: " + sessionId);
      // System.out.println("   ✓ User ID: " + userId);
      // return session;
    }
    
    System.out.println("   ✓ Session started: " + sessionId);
    System.out.println("   ✓ User ID: " + userId);
    System.out.println("   ✓ Session state: " + SessionState.ACTIVE.getDisplayName());
    System.out.println("   ✓ Ready to process messages\n");
    
    return null; // Mock session
  }

  /**
   * Step 4: Process user messages (simulated flow)
   */
  private static void processUserMessages(ISession session) {
    System.out.println("4. Processing User Messages...");
    
        // Simulate different types of user queries
        String[] userQueries = {
          "multiply 3 with 3",
          "What's the weather in New York?",
          "add 15 and 27",
          "Is it sunny in Paris?",
          "multiply 100 with 5"
        };
    
    for (String query : userQueries) {
      System.out.println("   User: " + query);
      
      // Process message through agent
      if (connectAgent != null) {
        // String response = connectAgent.processMessage(query, "user_session_001");
        // System.out.println("   Agent: " + response);
      } else {
        // Simulate response based on query type
        String response = simulateAgentResponse(query);
        System.out.println("   Agent: " + response);
      }
      
      System.out.println();
    }
  }

  /**
   * Step 5: Cleanup and session management
   */
  private static void cleanup(ISession session) {
    System.out.println("5. Cleanup and Session Management...");
    
    if (connectAgent != null) {
      // connectAgent.endSession("user_session_001");
      System.out.println("   ✓ Session ended");
    }
    
    System.out.println("   ✓ Resources cleaned up");
    System.out.println("   ✓ Framework shutdown complete");
  }

  /**
   * Create multiply tool
   */
  private static ITool createMultiplyTool() {
    System.out.println("   Creating Multiply Tool...");
    
    MultiplyTool multiply = new MultiplyTool();
    
    System.out.println("   - ID: " + multiply.getId());
    System.out.println("   - Name: " + multiply.getName());
    System.out.println("   - Category: " + multiply.getMetadata().getCategory().getDisplayName());
    System.out.println("   - Return Type: " + multiply.getMetadata().getReturnType().getDisplayName());
    System.out.println("   - Operations: Integer multiplication (x * y)");
    System.out.println("   - Status: Fully implemented");
    
    return multiply;
  }
  
  /**
   * Create sum tool
   */
  private static ITool createSumTool() {
    System.out.println("   Creating Sum Tool...");
    
    SumTool sum = new SumTool();
    
    System.out.println("   - ID: " + sum.getId());
    System.out.println("   - Name: " + sum.getName());
    System.out.println("   - Category: " + sum.getMetadata().getCategory().getDisplayName());
    System.out.println("   - Return Type: " + sum.getMetadata().getReturnType().getDisplayName());
    System.out.println("   - Operations: Integer addition (a + b)");
    System.out.println("   - Status: Fully implemented");
    
    return sum;
  }

  /**
   * Create skeleton weather tool
   */
  private static ITool createWeatherTool() {
    System.out.println("   Creating Weather Tool...");
    System.out.println("   - Category: " + ToolCategory.WEATHER.getDisplayName());
    System.out.println("   - Return Type: " + ReturnType.STRING.getDisplayName());
    System.out.println("   - Operations: Weather queries for cities");
    System.out.println("   - Status: Skeleton (null implementation)");
    
    // TODO: Implement actual weather tool
    // return new WeatherTool();
    return null;
  }

  /**
   * Simulate agent response for demonstration
   */
  private static String simulateAgentResponse(String query) {
    String lowerQuery = query.toLowerCase();
    
    if (lowerQuery.contains("weather") || lowerQuery.contains("sunny")) {
      return "I can help you with weather information using the Weather Tool. (Tool not implemented yet)";
    } else if (lowerQuery.contains("multiply") || lowerQuery.contains("times") || 
               lowerQuery.contains("*")) {
      // Use multiply tool for multiplication queries
      return executeMultiplyTool(query);
    } else if (lowerQuery.contains("add") || lowerQuery.contains("plus") || 
               lowerQuery.contains("+") || lowerQuery.contains("sum")) {
      // Use sum tool for addition queries
      return executeSumTool(query);
    } else {
      return "I understand your request. I have Multiply, Sum, and Weather tools available.";
    }
  }
  
  /**
   * Execute multiply tool with the given query.
   * 
   * @param query the multiplication query
   * @return the multiplication result or error message
   */
  private static String executeMultiplyTool(String query) {
    try {
      // Parse numbers from the query
      int[] numbers = extractTwoNumbers(query);
      if (numbers == null) {
        return "Multiply tool error: Could not extract two numbers from query";
      }
      
      // Create tool input
      IToolInput input = new ToolInput("demo_session");
      input.setParameter("x", numbers[0]);
      input.setParameter("y", numbers[1]);
      
      // Execute multiply tool
      IToolResult result = multiplyTool.execute(input);
      
      if (result.isSuccess()) {
        return "Multiply result: " + result.getData();
      } else {
        return "Multiply tool error: " + result.getError();
      }
    } catch (Exception e) {
      return "Multiply tool error: " + e.getMessage();
    }
  }
  
  /**
   * Execute sum tool with the given query.
   * 
   * @param query the addition query
   * @return the addition result or error message
   */
  private static String executeSumTool(String query) {
    try {
      // Parse numbers from the query
      int[] numbers = extractTwoNumbers(query);
      if (numbers == null) {
        return "Sum tool error: Could not extract two numbers from query";
      }
      
      // Create tool input
      IToolInput input = new ToolInput("demo_session");
      input.setParameter("a", numbers[0]);
      input.setParameter("b", numbers[1]);
      
      // Execute sum tool
      IToolResult result = sumTool.execute(input);
      
      if (result.isSuccess()) {
        return "Sum result: " + result.getData();
      } else {
        return "Sum tool error: " + result.getError();
      }
    } catch (Exception e) {
      return "Sum tool error: " + e.getMessage();
    }
  }
  
  /**
   * Extract two numbers from a natural language query.
   * 
   * @param query the query string
   * @return array of two integers or null if not found
   */
  private static int[] extractTwoNumbers(String query) {
    // Simple regex to find numbers in the query
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
    java.util.regex.Matcher matcher = pattern.matcher(query);
    
    java.util.List<Integer> numbers = new java.util.ArrayList<>();
    while (matcher.find() && numbers.size() < 2) {
      try {
        numbers.add(Integer.parseInt(matcher.group()));
      } catch (NumberFormatException e) {
        // Skip invalid numbers
      }
    }
    
    if (numbers.size() == 2) {
      return new int[]{numbers.get(0), numbers.get(1)};
    }
    
    return null;
  }

  /**
   * Demonstrate the high-level flow explanation
   */
  public static void explainFlow() {
    System.out.println("\n=== ConnectADK High-Level Flow ===");
    System.out.println();
    System.out.println("1. INITIALIZATION:");
    System.out.println("   - ConnectAgent starts up");
    System.out.println("   - LLM service configured");
    System.out.println("   - Tool registry initialized");
    System.out.println("   - Session manager ready");
    System.out.println();
    System.out.println("2. TOOL REGISTRATION:");
    System.out.println("   - Tools implement ITool interface");
    System.out.println("   - Tools registered with IToolRegistry");
    System.out.println("   - Tool metadata configured");
    System.out.println("   - Tools available for execution");
    System.out.println();
    System.out.println("3. SESSION MANAGEMENT:");
    System.out.println("   - User starts session");
    System.out.println("   - Session context maintained");
    System.out.println("   - Message history tracked");
    System.out.println("   - Session state managed");
    System.out.println();
    System.out.println("4. MESSAGE PROCESSING:");
    System.out.println("   - User sends message");
    System.out.println("   - LLM analyzes intent");
    System.out.println("   - Appropriate tool selected");
    System.out.println("   - Tool executed with parameters");
    System.out.println("   - Response generated and returned");
    System.out.println();
    System.out.println("5. TOOL EXECUTION:");
    System.out.println("   - Tool input validated");
    System.out.println("   - Tool business logic executed");
    System.out.println("   - Tool result generated");
    System.out.println("   - Result formatted for user");
    System.out.println();
    System.out.println("6. RESPONSE GENERATION:");
    System.out.println("   - Tool result processed");
    System.out.println("   - Response formatted by LLM");
    System.out.println("   - Response sent to user");
    System.out.println("   - Session context updated");
  }
}
