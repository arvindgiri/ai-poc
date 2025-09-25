package com.arv.framework.adk.interfaces.tool;

import java.util.List;
import java.util.Map;

/**
 * Interface for executing tools in a generic way.
 * The execution engine handles tool discovery, validation, and execution.
 */
public interface IToolExecutionEngine {
  
  /**
   * Execute a tool by name with the given parameters.
   * 
   * @param toolName the name of the tool to execute
   * @param parameters the parameters to pass to the tool
   * @return the result of tool execution
   */
  IToolResult executeTool(String toolName, Map<String, Object> parameters);
  
  /**
   * Get metadata for all available tools.
   * This is used to build tool schemas for LLM function calling.
   * 
   * @return list of tool metadata as Maps suitable for LLM APIs
   */
  List<Map<String, Object>> getAvailableToolSchemas();
  
  /**
   * Get names of all available tools.
   * 
   * @return list of tool names
   */
  List<String> getAvailableToolNames();
  
  /**
   * Check if a tool with the given name is available.
   * 
   * @param toolName the name of the tool to check
   * @return true if the tool is available, false otherwise
   */
  boolean isToolAvailable(String toolName);
  
  /**
   * Get metadata for a specific tool.
   * 
   * @param toolName the name of the tool
   * @return tool metadata as a Map, or null if tool not found
   */
  Map<String, Object> getToolSchema(String toolName);
}