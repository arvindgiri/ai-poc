// Main class for running the MultiToolAgent application

import agents.multitool.MultiToolAgent2;

/**
 * Main class to run the MultiToolAgent with proper system variable configuration. This class sets
 * up the required environment variables for Google GenAI API.
 */
public class Main {

  /**
   * Main method to run the MultiToolAgent application. Sets up system variables and starts the
   * agent.
   *
   * @param args command line arguments
   * @throws Exception if there's an error running the agent
   */
  public static void main2(String[] args) throws Exception {
    // Set system variables for Google GenAI API
    System.setProperty("GOOGLE_GENAI_USE_VERTEXAI", "FALSE");
    System.setProperty("GOOGLE_API_KEY", "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U");
    System.out.println("Starting MultiToolAgent with Google GenAI API...");
    System.out.println(
        "GOOGLE_GENAI_USE_VERTEXAI: " + System.getProperty("GOOGLE_GENAI_USE_VERTEXAI"));
    System.out.println(
        "GOOGLE_API_KEY: " + (System.getProperty("GOOGLE_API_KEY") != null ? "Set" : "Not set"));

    // Run the MultiToolAgent
    MultiToolAgent2.main(args);
  }
}
