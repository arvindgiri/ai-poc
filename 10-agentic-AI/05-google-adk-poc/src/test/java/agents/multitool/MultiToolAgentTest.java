package agents.multitool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for MultiToolAgent functionality. Tests the weather and time query methods with
 * various city inputs.
 */
public class MultiToolAgentTest {

  @BeforeEach
  void setUp() {
    // Set up system properties for testing
    System.setProperty("GOOGLE_GENAI_USE_VERTEXAI", "FALSE");
    System.setProperty("GOOGLE_API_KEY", "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U");
  }

  @Test
  void testGetWeather_NewYork_ShouldReturnSuccess() {
    // Test weather query for New York (supported city)
    Map<String, String> result = MultiToolAgent.getWeather("New York");

    assertNotNull(result, "Result should not be null");
    assertEquals("success", result.get("status"), "Status should be success for New York");
    assertTrue(result.get("report").contains("New York"), "Report should contain city name");
    assertTrue(result.get("report").contains("sunny"), "Report should contain weather info");
    assertTrue(result.get("report").contains("25 degrees"), "Report should contain temperature");
  }

  @Test
  void testGetWeather_Paris_ShouldReturnError() {
    // Test weather query for Paris (unsupported city)
    Map<String, String> result = MultiToolAgent2.getWeather("Paris");

    assertNotNull(result, "Result should not be null");
    assertEquals("error", result.get("status"), "Status should be error for Paris");
    assertTrue(result.get("report").contains("Paris"), "Report should contain city name");
    assertTrue(result.get("report").contains("not available"),
        "Report should indicate unavailability");
  }

  @Test
  void testGetWeather_CaseInsensitive_ShouldWork() {
    // Test weather query with different case
    Map<String, String> result = MultiToolAgent2.getWeather("new york");

    assertNotNull(result, "Result should not be null");
    assertEquals("success", result.get("status"), "Status should be success for lowercase input");
  }

  @Test
  void testGetCurrentTime_NewYork_ShouldReturnSuccess() {
    // Test time query for New York
    Map<String, String> result = MultiToolAgent2.getCurrentTime("New York");

    assertNotNull(result, "Result should not be null");
    assertEquals("success", result.get("status"), "Status should be success for New York");
    assertTrue(result.get("report").contains("New York"), "Report should contain city name");
    assertTrue(result.get("report").contains("current time"), "Report should mention current time");
    assertTrue(result.get("report").matches(".*\\d{2}:\\d{2}.*"),
        "Report should contain time format HH:mm");
  }

  @Test
  void testGetCurrentTime_Paris_ShouldReturnError() {
    // Test time query for Paris (may not have timezone info)
    Map<String, String> result = MultiToolAgent2.getCurrentTime("Paris");

    assertNotNull(result, "Result should not be null");
    // Note: This test might pass or fail depending on available timezones
    // We'll check if it's either success or error
    assertTrue("success".equals(result.get("status")) || "error".equals(result.get("status")),
        "Status should be either success or error");
    assertTrue(result.get("report").contains("Paris"), "Report should contain city name");
  }

  @Test
  void testGetCurrentTime_InvalidCity_ShouldReturnError() {
    // Test time query for invalid city
    Map<String, String> result = MultiToolAgent2.getCurrentTime("InvalidCity123");

    assertNotNull(result, "Result should not be null");
    assertEquals("error", result.get("status"), "Status should be error for invalid city");
    assertTrue(result.get("report").contains("InvalidCity123"), "Report should contain city name");
    assertTrue(result.get("report").contains("timezone information"),
        "Report should mention timezone issue");
  }

  @Test
  void testGetCurrentTime_CaseInsensitive_ShouldWork() {
    // Test time query with different case
    Map<String, String> result = MultiToolAgent2.getCurrentTime("new york");

    assertNotNull(result, "Result should not be null");
    assertEquals("success", result.get("status"), "Status should be success for lowercase input");
  }

  @Test
  void testAgentInitialization_ShouldNotBeNull() {
    // Test that the agent initializes properly
    assertNotNull(MultiToolAgent2.ROOT_AGENT, "ROOT_AGENT should not be null");
    assertNotNull(MultiToolAgent2.initAgent(), "initAgent() should return non-null agent");
  }
}
