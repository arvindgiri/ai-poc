package agents.multitool;

import java.util.Map;

/**
 * Simple test runner to demonstrate the MultiToolAgent functionality.
 * This class runs the test cases manually and displays results.
 */
public class TestRunner {

  /**
   * Main method to run test cases manually.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    // Set up system properties
    System.setProperty("GOOGLE_GENAI_USE_VERTEXAI", "FALSE");
    System.setProperty("GOOGLE_API_KEY", "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U");
    
    System.out.println("=== MultiToolAgent Test Runner ===\n");
    
    // Test cases as specified by the user
    String[] testQueries = {
        "What is the weather in New York?",
        "What is the time in New York?",
        "What is the weather in Paris?",
        "What is the time in Paris?"
    };
    
    // Run each test case
    for (String query : testQueries) {
      System.out.println("Testing: " + query);
      runTestQuery(query);
      System.out.println("---");
    }
    
    // Test individual methods
    System.out.println("\n=== Direct Method Tests ===");
    testWeatherMethods();
    testTimeMethods();
  }
  
  /**
   * Runs a test query by calling the appropriate methods directly.
   *
   * @param query the test query to process
   */
  private static void runTestQuery(String query) {
    String lowerQuery = query.toLowerCase();
    
    if (lowerQuery.contains("weather")) {
      String city = extractCity(query);
      Map<String, String> result = MultiToolAgent2.getWeather(city);
      System.out.println("Weather Result: " + result);
    } else if (lowerQuery.contains("time")) {
      String city = extractCity(query);
      Map<String, String> result = MultiToolAgent2.getCurrentTime(city);
      System.out.println("Time Result: " + result);
    } else {
      System.out.println("Unknown query type: " + query);
    }
  }
  
  /**
   * Extracts city name from a query string.
   *
   * @param query the query string
   * @return the extracted city name
   */
  private static String extractCity(String query) {
    // Simple extraction - looks for "in [City]"
    String[] parts = query.split(" in ");
    if (parts.length > 1) {
      String cityPart = parts[1].replace("?", "").trim();
      return cityPart;
    }
    return "Unknown";
  }
  
  /**
   * Tests weather-related methods directly.
   */
  private static void testWeatherMethods() {
    System.out.println("Testing Weather Methods:");
    
    // Test New York weather
    Map<String, String> nyWeather = MultiToolAgent2.getWeather("New York");
    System.out.println("New York Weather: " + nyWeather);
    
    // Test Paris weather
    Map<String, String> parisWeather = MultiToolAgent2.getWeather("Paris");
    System.out.println("Paris Weather: " + parisWeather);
  }
  
  /**
   * Tests time-related methods directly.
   */
  private static void testTimeMethods() {
    System.out.println("Testing Time Methods:");
    
    // Test New York time
    Map<String, String> nyTime = MultiToolAgent2.getCurrentTime("New York");
    System.out.println("New York Time: " + nyTime);
    
    // Test Paris time
    Map<String, String> parisTime = MultiToolAgent2.getCurrentTime("Paris");
    System.out.println("Paris Time: " + parisTime);
  }
}
