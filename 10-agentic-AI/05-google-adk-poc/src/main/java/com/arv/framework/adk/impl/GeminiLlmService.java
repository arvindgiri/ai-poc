package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.gemini.ILlmConfig;
import com.arv.framework.adk.interfaces.gemini.ILlmService;
import com.arv.framework.adk.prompt.PromptBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implementation of ILlmService using Google Gemini API via HTTP calls. This implementation makes
 * direct HTTP requests to the Gemini API.
 */
@Slf4j
public class GeminiLlmService implements ILlmService {

  private static final String GEMINI_API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private ILlmConfig config;
  private boolean initialized = false;
  private OkHttpClient httpClient;
  private ObjectMapper objectMapper;

  public GeminiLlmService(ILlmConfig config) {
    this.config = config;
    this.objectMapper = new ObjectMapper();
    initialize(config);
  }

  @Override
  public void initialize(ILlmConfig config) {
    log.debug("initialize method called");
    this.config = config;
    this.initialized = (config != null && config.getApiKey() != null);

    if (this.initialized) {
      // Initialize HTTP client with timeouts
      this.httpClient = new OkHttpClient.Builder()
          .connectTimeout(30, TimeUnit.SECONDS)
          .readTimeout(60, TimeUnit.SECONDS)
          .writeTimeout(30, TimeUnit.SECONDS)
          .build();
    }
  }

  @Override
  public ILlmConfig getConfig() {
    return config;
  }

  @Override
  public void setConfig(ILlmConfig config) {
    this.config = config;
    this.initialized = (config != null && config.getApiKey() != null);
  }

  @Override
  public String generateResponse(String prompt, Map<String, Object> context) {
    log.debug("Generating response for prompt: {}", prompt);

    if (!initialized) {
      log.debug("LLM service not initialized");
      return "Error: LLM service not initialized";
    }

    if (config == null || config.getApiKey() == null || config.getApiKey().trim().isEmpty()) {
      log.error("API key not configured");
      return "Error: API key not configured";
    }

    try {
      // Build the API URL
      String model = config.getModel() != null ? config.getModel() : "gemini-2.0-flash";
      String url = GEMINI_API_BASE_URL + model + ":generateContent?key=" + config.getApiKey();

      // Build the request payload
      // Build prompt using PromptBuilder
      PromptBuilder promptBuilder = new PromptBuilder()
          .withUserMessage(prompt)
          .withGenerationConfig(config.getTemperature(), config.getMaxTokens());

      // Add system instruction for better tool usage
      if (context.containsKey("availableTools")) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tools = (List<Map<String, Object>>) context.get("availableTools");
        if (!tools.isEmpty()) {
          // Extract tool names for system instruction
          List<String> toolNames = new ArrayList<>();
          for (Map<String, Object> tool : tools) {
            toolNames.add((String) tool.get("name"));
          }
          
          // Add system instruction and tools
          promptBuilder.withSystemInstruction(PromptBuilder.createDefaultSystemInstruction(toolNames))
                      .withTools(tools);
        }
      }

      ObjectNode requestBody = promptBuilder.build();

      // Create the HTTP request
      RequestBody body = RequestBody.create(requestBody.toString(), JSON);
      Request request = new Request.Builder()
          .url(url)
          .post(body)
          .addHeader("Content-Type", "application/json")
          .build();

      // Execute the request  
      try {
        // Create a copy of request body without tools for cleaner logging
        ObjectNode logRequestBody = requestBody.deepCopy();
        logRequestBody.remove("tools"); // Remove tools section from logs
        log.info("Gemini Request:\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logRequestBody));
      } catch (Exception e) {
        log.info("Gemini Request: " + requestBody.toString()); // Fallback to compact format
      }
      try (Response response = httpClient.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          String errorBody = response.body() != null ? response.body().string() : "Unknown error";
          log.error("API call failed with status {}: {}", response.code(), errorBody);
          return "Error: API call failed with status " + response.code() + ": " + errorBody;
        }

        // Parse the response
        String responseBody = response.body().string();
        log.info("Received response from Gemini API");
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        
        // Log the pretty-printed response
        try {
          log.info("Gemini Response:\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse));
        } catch (Exception e) {
          log.info("Gemini Response: " + responseBody); // Fallback to raw response
        }

        // Extract the generated text or function calls
        JsonNode candidates = jsonResponse.get("candidates");
        if (candidates != null && candidates.isArray() && candidates.size() > 0) {
          JsonNode firstCandidate = candidates.get(0);
          JsonNode responseContent = firstCandidate.get("content");
          if (responseContent != null) {
            JsonNode responseParts = responseContent.get("parts");
            if (responseParts != null && responseParts.isArray() && responseParts.size() > 0) {
              JsonNode firstPart = responseParts.get(0);
              
              // Check if this is a function call
              if (firstPart.has("functionCall")) {
                JsonNode functionCall = firstPart.get("functionCall");
                String functionName = functionCall.get("name").asText();
                JsonNode args = functionCall.get("args");
                
                log.info("Gemini suggested function call: {} with args: {}", functionName, args);
                
                // Return function call information as JSON string
                ObjectNode result = objectMapper.createObjectNode();
                result.put("type", "function_call");
                result.put("function_name", functionName);
                result.set("arguments", args);
                return result.toString();
              }
              
              // Regular text response
              JsonNode text = firstPart.get("text");
              if (text != null) {
                String result = text.asText().trim();
                log.debug("Successfully generated response of length: {}", result);
                return result;
              }
            }
          }
        }

        log.debug("Unable to parse response from Gemini API");
        return "Error: Unable to parse response from Gemini API";
      }

    } catch (IOException e) {
      log.error("Failed to communicate with Gemini API", e);
      return "Error: Failed to communicate with Gemini API - " + e.getMessage();
    } catch (Exception e) {
      log.error("Unexpected error during response generation", e);
      return "Error: Unexpected error - " + e.getMessage();
    }
  }

  // Simple method for backward compatibility
  public String generateResponse(String prompt) {
    return generateResponse(prompt, Map.of());
  }

  @Override
  public String analyzeIntent(String message, List<String> availableTools) {
    if (!initialized) {
      return "Error: LLM service not initialized";
    }

    String prompt = "Analyze the user's intent from this message: \"" + message + "\"\n" +
        "Available tools: " + String.join(", ", availableTools) + "\n" +
        "Provide a brief analysis of what the user wants to accomplish.";

    String response = generateResponse(prompt);
    return response.startsWith("Error:") ? "User wants to: " + message : response;
  }

  @Override
  public List<String> suggestTools(String message, List<String> availableTools) {
    List<String> suggestions = new ArrayList<>();

    if (!initialized || availableTools.isEmpty()) {
      return suggestions;
    }

    String prompt = "Given the user message: \"" + message + "\"\n" +
        "And these available tools: " + String.join(", ", availableTools) + "\n" +
        "Which tools would be most appropriate? Respond with only the tool names, separated by commas.";

    String response = generateResponse(prompt);

    if (!response.startsWith("Error:")) {
      String[] suggestedTools = response.split(",");
      for (String tool : suggestedTools) {
        String trimmedTool = tool.trim();
        if (availableTools.contains(trimmedTool)) {
          suggestions.add(trimmedTool);
        }
      }
    }

    // Fallback to simple keyword matching
    if (suggestions.isEmpty()) {
      String lowerMessage = message.toLowerCase();
      for (String tool : availableTools) {
        if (lowerMessage.contains(tool.toLowerCase())) {
          suggestions.add(tool);
        }
      }
    }

    return suggestions;
  }

  @Override
  public Map<String, Object> extractParameters(String message, String toolMetadata) {
    if (!initialized) {
      return Map.of();
    }

    String prompt = "Extract parameters from this user message: \"" + message + "\"\n" +
        "Tool metadata: " + toolMetadata + "\n" +
        "Return the parameters in JSON format.";

    String response = generateResponse(prompt);

    // For now, return empty map - could implement JSON parsing here
    // This would require more sophisticated parsing of the LLM response
    return Map.of();
  }

  @Override
  public boolean validateResponse(String response) {
    return response != null && !response.trim().isEmpty() &&
        !response.toLowerCase().contains("error");
  }

  @Override
  public String getModel() {
    return config != null ? config.getModel() : null;
  }

  @Override
  public void setModel(String model) {
    if (config != null) {
      config.setModel(model);
    }
  }

  @Override
  public double getTemperature() {
    return config != null ? config.getTemperature() : 0.7;
  }

  @Override
  public void setTemperature(double temperature) {
    if (config != null) {
      config.setTemperature(temperature);
    }
  }

  @Override
  public int getMaxTokens() {
    return config != null ? config.getMaxTokens() : 1000;
  }

  @Override
  public void setMaxTokens(int maxTokens) {
    if (config != null) {
      config.setMaxTokens(maxTokens);
    }
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public String getStatus() {
    if (!initialized) {
      return "Not initialized";
    }
    if (config == null || !config.isValid()) {
      return "Invalid configuration";
    }
    return "Ready";
  }

  @Override
  public void reset() {
    this.initialized = false;
    if (config != null) {
      // Reset to default values
      config.setTemperature(0.7);
      config.setMaxTokens(1000);
      config.setMaxRetries(3);
      config.setTimeoutMs(30000L);
    }
  }
}
