package com.arv.framework.adk.prompt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Builder class for constructing structured prompts for Gemini API.
 * Supports system instructions, user messages, conversation history, and tool definitions.
 */
@Slf4j
public class PromptBuilder {
  
  private final ObjectMapper objectMapper;
  private ObjectNode promptObject;
  private ArrayNode contentsArray;
  private ObjectNode systemInstruction;
  private ArrayNode toolsArray;
  
  /**
   * Creates a new PromptBuilder instance.
   */
  public PromptBuilder() {
    this.objectMapper = new ObjectMapper();
    this.promptObject = objectMapper.createObjectNode();
    this.contentsArray = objectMapper.createArrayNode();
    this.promptObject.set("contents", contentsArray);
  }
  
  /**
   * Adds system instruction to the prompt.
   * 
   * @param instruction the system instruction text
   * @return this builder for method chaining
   */
  public PromptBuilder withSystemInstruction(String instruction) {
    log.debug("Adding system instruction: {}", instruction);
    
    systemInstruction = objectMapper.createObjectNode();
    systemInstruction.put("role", "system");
    
    ArrayNode partsArray = objectMapper.createArrayNode();
    ObjectNode textPart = objectMapper.createObjectNode();
    textPart.put("text", instruction);
    partsArray.add(textPart);
    
    systemInstruction.set("parts", partsArray);
    promptObject.set("systemInstruction", systemInstruction);
    
    return this;
  }
  
  /**
   * Adds a user message to the conversation.
   * 
   * @param message the user message text
   * @return this builder for method chaining
   */
  public PromptBuilder withUserMessage(String message) {
    log.debug("Adding user message: {}", message);
    
    ObjectNode contentNode = objectMapper.createObjectNode();
    contentNode.put("role", "user");
    
    ArrayNode partsArray = objectMapper.createArrayNode();
    ObjectNode textPart = objectMapper.createObjectNode();
    textPart.put("text", message);
    partsArray.add(textPart);
    
    contentNode.set("parts", partsArray);
    contentsArray.add(contentNode);
    
    return this;
  }
  
  /**
   * Adds an assistant message to the conversation.
   * 
   * @param message the assistant message text
   * @return this builder for method chaining
   */
  public PromptBuilder withAssistantMessage(String message) {
    log.debug("Adding assistant message: {}", message);
    
    ObjectNode contentNode = objectMapper.createObjectNode();
    contentNode.put("role", "model");
    
    ArrayNode partsArray = objectMapper.createArrayNode();
    ObjectNode textPart = objectMapper.createObjectNode();
    textPart.put("text", message);
    partsArray.add(textPart);
    
    contentNode.set("parts", partsArray);
    contentsArray.add(contentNode);
    
    return this;
  }
  
  /**
   * Adds conversation history to the prompt.
   * 
   * @param history list of conversation messages in format "Role: Message"
   * @return this builder for method chaining
   */
  public PromptBuilder withConversationHistory(List<String> history) {
    log.debug("Adding conversation history with {} messages", history.size());
    
    for (String message : history) {
      if (message.startsWith("User: ")) {
        withUserMessage(message.substring(6));
      } else if (message.startsWith("Assistant: ")) {
        withAssistantMessage(message.substring(11));
      }
    }
    
    return this;
  }
  
  /**
   * Adds tools/functions that Gemini can call.
   * 
   * @param tools list of tool definitions as Maps
   * @return this builder for method chaining
   */
  public PromptBuilder withTools(List<Map<String, Object>> tools) {
    if (tools == null || tools.isEmpty()) {
      log.debug("No tools to add");
      return this;
    }
    
    log.debug("Adding {} tools", tools.size());
    
    if (toolsArray == null) {
      toolsArray = objectMapper.createArrayNode();
      promptObject.set("tools", toolsArray);
    }
    
    // Create tools object with function_declarations
    ObjectNode toolsObject = objectMapper.createObjectNode();
    ArrayNode functionDeclarations = objectMapper.createArrayNode();
    
    for (Map<String, Object> tool : tools) {
      JsonNode toolNode = objectMapper.valueToTree(tool);
      functionDeclarations.add(toolNode);
    }
    
    toolsObject.set("function_declarations", functionDeclarations);
    toolsArray.add(toolsObject);
    
    return this;
  }
  
  /**
   * Adds generation configuration to the prompt.
   * 
   * @param temperature the temperature value (0.0 to 1.0)
   * @param maxOutputTokens maximum output tokens
   * @return this builder for method chaining
   */
  public PromptBuilder withGenerationConfig(double temperature, int maxOutputTokens) {
    log.debug("Adding generation config: temperature={}, maxTokens={}", temperature, maxOutputTokens);
    
    ObjectNode generationConfig = objectMapper.createObjectNode();
    generationConfig.put("temperature", temperature);
    generationConfig.put("maxOutputTokens", maxOutputTokens);
    
    promptObject.set("generationConfig", generationConfig);
    
    return this;
  }
  
  /**
   * Builds the final prompt as an ObjectNode.
   * 
   * @return the constructed prompt object
   */
  public ObjectNode build() {
    log.debug("Building prompt with {} content entries", contentsArray.size());
    return promptObject;
  }
  
  /**
   * Creates a default system instruction for an AI assistant with tools.
   * 
   * @param availableTools list of tool names that are available
   * @return formatted system instruction
   */
  public static String createDefaultSystemInstruction(List<String> availableTools) {
    StringBuilder instruction = new StringBuilder();
    instruction.append("You are an intelligent assistant with access to various tools. ");
    
    if (availableTools != null && !availableTools.isEmpty()) {
      instruction.append("Available tools: ");
      instruction.append(String.join(", ", availableTools));
      instruction.append(". ");
    }
    
    instruction.append("When the user asks something, decide whether to call a tool or provide a final answer. ");
    instruction.append("If you need to use a tool, respond with a function call using the correct arguments. ");
    instruction.append("If you have enough information to answer directly, provide a clear and helpful response. ");
    instruction.append("Always be helpful, accurate, and concise in your responses.");
    
    return instruction.toString();
  }
  
  /**
   * Creates a new PromptBuilder with default configuration.
   * 
   * @param userMessage the initial user message
   * @param availableTools list of available tools
   * @return configured PromptBuilder
   */
  public static PromptBuilder createDefault(String userMessage, List<String> availableTools) {
    String systemInstruction = createDefaultSystemInstruction(availableTools);
    
    return new PromptBuilder()
        .withSystemInstruction(systemInstruction)
        .withUserMessage(userMessage)
        .withGenerationConfig(0.7, 1000);
  }
}
