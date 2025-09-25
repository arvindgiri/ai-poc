package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.core.IToolRegistry;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolExecutionEngine;
import com.arv.framework.adk.interfaces.tool.IToolInput;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;
import com.arv.framework.adk.interfaces.tool.IToolResult;
import com.arv.framework.adk.interfaces.tool.IParameter;
import com.arv.framework.adk.tools.ToolInput;
import com.arv.framework.adk.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of IToolExecutionEngine that uses IToolRegistry
 * to discover and execute tools in a generic way.
 */
@Slf4j
public class ToolExecutionEngine implements IToolExecutionEngine {
  
  private final IToolRegistry toolRegistry;
  
  /**
   * Creates a new ToolExecutionEngine with the given tool registry.
   * 
   * @param toolRegistry the tool registry to use for tool discovery
   */
  public ToolExecutionEngine(IToolRegistry toolRegistry) {
    this.toolRegistry = toolRegistry;
  }
  
  @Override
  public IToolResult executeTool(String toolName, Map<String, Object> parameters) {
    log.debug("Executing tool: {} with parameters: {}", toolName, parameters);
    
    // Get the tool from registry
    Optional<ITool> toolOpt = toolRegistry.getTool(toolName);
    if (!toolOpt.isPresent()) {
      log.debug("Tool not found: {}", toolName);
      return ToolResult.failure("Tool not found: " + toolName);
    }
    
    ITool tool = toolOpt.get();
    
    // Check if tool is enabled
    if (!toolRegistry.isToolEnabled(toolName)) {
      log.debug("Tool is disabled: {}", toolName);
      return ToolResult.failure("Tool is disabled: " + toolName);
    }
    
    try {
      // Create tool input and set parameters
      String sessionId = (String) parameters.get("sessionId");
      IToolInput input = new ToolInput(sessionId);
      
      // Set all parameters except sessionId
      for (Map.Entry<String, Object> entry : parameters.entrySet()) {
        if (!"sessionId".equals(entry.getKey())) {
          input.setParameter(entry.getKey(), entry.getValue());
        }
      }
      
      // Validate input
      boolean isValidInput = tool.validateInput(input);
      if (!isValidInput) {
        log.debug("Tool input validation failed for {}", toolName);
        return ToolResult.failure("Invalid input parameters for tool: " + toolName);
      }
      
      // Execute the tool
      log.debug("Tool validation passed, executing tool: {}", toolName);
      IToolResult result = tool.execute(input);
      
      log.debug("Tool execution completed for {}: success={}", toolName, result.isSuccess());
      return result;
      
    } catch (Exception e) {
      log.error("Error executing tool {}: {}", toolName, e.getMessage(), e);
      return ToolResult.failure("Tool execution error: " + e.getMessage());
    }
  }
  
  @Override
  public List<Map<String, Object>> getAvailableToolSchemas() {
    log.debug("Getting available tool schemas");
    
    List<Map<String, Object>> schemas = new ArrayList<>();
    List<ITool> enabledTools = toolRegistry.getEnabledTools();
    
    for (ITool tool : enabledTools) {
      try {
        IToolMetadata metadata = tool.getMetadata();
        Map<String, Object> schema = convertMetadataToSchema(metadata);
        schemas.add(schema);
        log.debug("Added schema for tool: {}", metadata.getId());
      } catch (Exception e) {
        log.warn("Failed to get schema for tool {}: {}", tool.getId(), e.getMessage());
      }
    }
    
    log.debug("Retrieved {} tool schemas", schemas.size());
    return schemas;
  }
  
  @Override
  public List<String> getAvailableToolNames() {
    log.debug("Getting available tool names");
    
    List<String> toolNames = new ArrayList<>();
    List<ITool> enabledTools = toolRegistry.getEnabledTools();
    
    for (ITool tool : enabledTools) {
      toolNames.add(tool.getId());
    }
    
    log.debug("Retrieved {} tool names", toolNames.size());
    return toolNames;
  }
  
  @Override
  public boolean isToolAvailable(String toolName) {
    return toolRegistry.isToolRegistered(toolName) && toolRegistry.isToolEnabled(toolName);
  }
  
  @Override
  public Map<String, Object> getToolSchema(String toolName) {
    log.debug("Getting schema for tool: {}", toolName);
    
    Optional<ITool> toolOpt = toolRegistry.getTool(toolName);
    if (!toolOpt.isPresent() || !toolRegistry.isToolEnabled(toolName)) {
      log.debug("Tool not available: {}", toolName);
      return null;
    }
    
    try {
      IToolMetadata metadata = toolOpt.get().getMetadata();
      return convertMetadataToSchema(metadata);
    } catch (Exception e) {
      log.warn("Failed to get schema for tool {}: {}", toolName, e.getMessage());
      return null;
    }
  }
  
  /**
   * Converts tool metadata to a schema format suitable for LLM APIs.
   * 
   * @param metadata the tool metadata
   * @return schema as a Map
   */
  private Map<String, Object> convertMetadataToSchema(IToolMetadata metadata) {
    Map<String, Object> schema = new HashMap<>();
    
    schema.put("name", metadata.getId());
    schema.put("description", metadata.getDescription());
    
    // Build parameters schema
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("type", "object");
    
    Map<String, Object> properties = new HashMap<>();
    List<String> required = new ArrayList<>();
    
    for (IParameter param : metadata.getParameters()) {
      Map<String, Object> paramSchema = new HashMap<>();
      paramSchema.put("type", mapParameterType(param.getType()));
      paramSchema.put("description", param.getDescription());
      
      properties.put(param.getName(), paramSchema);
      
      if (param.isRequired()) {
        required.add(param.getName());
      }
    }
    
    parameters.put("properties", properties);
    if (!required.isEmpty()) {
      parameters.put("required", required);
    }
    
    schema.put("parameters", parameters);
    
    return schema;
  }
  
  /**
   * Maps parameter types to JSON schema types.
   * 
   * @param paramType the parameter type
   * @return JSON schema type
   */
  private String mapParameterType(String paramType) {
    if (paramType == null) {
      return "string";
    }
    
    switch (paramType.toLowerCase()) {
      case "int":
      case "integer":
      case "long":
        return "integer";
      case "double":
      case "float":
      case "number":
        return "number";
      case "bool":
      case "boolean":
        return "boolean";
      case "array":
      case "list":
        return "array";
      case "object":
      case "map":
        return "object";
      default:
        return "string";
    }
  }
}
