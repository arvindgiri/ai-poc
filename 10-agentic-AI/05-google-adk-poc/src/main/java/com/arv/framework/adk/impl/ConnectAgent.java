package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.core.IAgent;
import com.arv.framework.adk.interfaces.core.ISessionManager;
import com.arv.framework.adk.interfaces.gemini.ILlmService;
import com.arv.framework.adk.interfaces.session.ISession;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolExecutionEngine;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Main implementation of IAgent using ConnectADK framework.
 * This agent is completely tool-agnostic and uses ToolExecutionEngine for all tool operations.
 */
@Slf4j
public class ConnectAgent implements IAgent {

  private ILlmService llmService;
  private ISessionManager sessionManager;
  private IToolExecutionEngine toolExecutionEngine;
  private ObjectMapper objectMapper;

  public ConnectAgent(ILlmService llmService, IToolExecutionEngine toolExecutionEngine,
      ISessionManager sessionManager) {
    this.llmService = llmService;
    this.toolExecutionEngine = toolExecutionEngine;
    this.sessionManager = sessionManager;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public boolean registerTool(ITool tool) {
    // Tool registration is handled by the ToolExecutionEngine's registry
    // This method is kept for interface compatibility but delegates to the execution engine
    throw new UnsupportedOperationException("Tool registration should be done through ToolExecutionEngine's registry");
  }

  @Override
  public boolean unregisterTool(String toolId) {
    // Tool unregistration is handled by the ToolExecutionEngine's registry
    throw new UnsupportedOperationException("Tool unregistration should be done through ToolExecutionEngine's registry");
  }

  @Override
  public Object processMessage(String message, String sessionId) {
    log.debug("Processing message for session {}: {}", sessionId, message);

    try {
      // Use multi-turn conversation to handle complex interactions
      return processMultiTurnConversation(message, sessionId);
    } catch (Exception e) {
      log.error("Error processing message: {}", e.getMessage(), e);
      return "Sorry, I encountered an error while processing your request.";
    }
  }

  /**
   * Processes a message using multi-turn conversation with Gemini.
   * This allows for tool calls and follow-up responses.
   */
  private Object processMultiTurnConversation(String initialMessage, String sessionId) {
    List<String> conversationHistory = new ArrayList<>();
    conversationHistory.add("User: " + initialMessage);

    String currentMessage = initialMessage;
    int maxTurns = 10; // Prevent infinite loops
    int turnCount = 0;

    while (turnCount < maxTurns) {
      turnCount++;
      log.debug("Multi-turn conversation - Turn {}: {}", turnCount, currentMessage);

      // Prepare available tools for Gemini
      Map<String, Object> context = new HashMap<>();
      List<Map<String, Object>> availableTools = toolExecutionEngine.getAvailableToolSchemas();
      if (!availableTools.isEmpty()) {
        context.put("availableTools", availableTools);
        log.debug("Sending {} tools to Gemini on turn {}", availableTools.size(), turnCount);
      }

      // Add conversation history to context for continuity
      if (conversationHistory.size() > 1) {
        context.put("conversation_history", String.join("\n", conversationHistory));
      }

      String response = llmService.generateResponse(currentMessage, context);

      // Check if response is a function call
      if (response.startsWith("{") && response.contains("function_call")) {
        try {
          JsonNode responseJson = objectMapper.readTree(response);
          if (responseJson.has("function_call")) {
            JsonNode functionCall = responseJson.get("function_call");
            String functionName = functionCall.get("name").asText();
            JsonNode arguments = functionCall.get("arguments");

            log.debug("Turn {} - Executing function call: {} with args: {}", turnCount, functionName, arguments);

            String toolResult = executeFunctionCall(functionName, arguments, sessionId);

            // Add tool execution to conversation history
            conversationHistory.add("Assistant: Called " + functionName + "(" + arguments + ")");
            conversationHistory.add("Tool Result: " + toolResult);

            // Continue the conversation with the tool result
            currentMessage = "Based on the tool result: " + toolResult +
                "\n Please continue with original request";

            log.debug("Turn {} - Tool result: {}", turnCount, toolResult);
            continue;
          }
        } catch (Exception e) {
          log.error("Error parsing function call response on turn {}", turnCount, e);
          break;
        }
      }

      // If we reach here, we have a final response
      conversationHistory.add("Assistant: " + response);
      log.debug("Multi-turn conversation completed after {} turns", turnCount);
      return response;
    }

    log.debug("Multi-turn conversation reached maximum turns ({})", maxTurns);
    return "I apologize, but I couldn't complete the request within the allowed number of steps.";
  }

  /**
   * Executes a function call using the ToolExecutionEngine.
   */
  private String executeFunctionCall(String functionName, JsonNode arguments, String sessionId) {
    log.debug("Executing function call: {} with arguments: {}", functionName, arguments);

    try {
      // Convert JsonNode arguments to Map
      Map<String, Object> parameters = new HashMap<>();
      arguments.fields().forEachRemaining(entry -> {
        String key = entry.getKey();
        JsonNode value = entry.getValue();

        if (value.isNumber()) {
          parameters.put(key, value.asLong()); // Use Long to handle large numbers
        } else if (value.isTextual()) {
          parameters.put(key, value.asText());
        } else if (value.isBoolean()) {
          parameters.put(key, value.asBoolean());
        } else {
          parameters.put(key, value.toString());
        }
      });

      // Add session context if needed
      parameters.put("sessionId", sessionId);

      // Execute tool using the execution engine
      IToolResult result = toolExecutionEngine.executeTool(functionName, parameters);
      
      if (result.isSuccess()) {
        log.debug("Tool execution successful: {}", result.getData());
        return result.getData();
      } else {
        log.error("Tool execution failed: {}", result.getError());
        return "Error executing " + functionName + ": " + result.getError();
      }

    } catch (Exception e) {
      log.error("Exception during function call execution", e);
      return "Error executing " + functionName + ": " + e.getMessage();
    }
  }

  @Override
  public ISession startSession(String sessionId) {
    return sessionManager.createSession(sessionId);
  }

  @Override
  public boolean endSession(String sessionId) {
    sessionManager.endSession(sessionId);
    return true;
  }

  @Override
  public void setLlmApiKey(String apiKey) {
    if (llmService.getConfig() != null) {
      llmService.getConfig().setApiKey(apiKey);
    }
  }

  @Override
  public void setLlmModel(String model) {
    if (llmService.getConfig() != null) {
      llmService.getConfig().setModel(model);
    }
  }
}