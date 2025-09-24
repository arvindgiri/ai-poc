package com.arv.adk.interfaces.tool;
import java.util.Map;

/**
 * Executes tools and manages their lifecycle infrastructure.
 * This interface defines the contract for tool execution in the ConnectADK framework.
 */
public interface IToolExecutionEngine {
    
    /**
     * Executes a tool with the given input.
     * 
     * @param toolId the tool ID
     * @param input the tool input
     * @param sessionId the session ID
     * @return the tool result
     */
    IToolResult executeTool(String toolId, IToolInput input, String sessionId);
    
    /**
     * Executes a tool with parameters.
     * 
     * @param tool the tool to execute
     * @param parameters the parameters
     * @param sessionId the session ID
     * @return the tool result
     */
    IToolResult executeTool(ITool tool, Map<String, Object> parameters, String sessionId);
    
    /**
     * Validates tool execution before running.
     * 
     * @param tool the tool to validate
     * @param input the tool input
     * @return true if validation passed, false otherwise
     */
    boolean validateToolExecution(ITool tool, IToolInput input);
    
    /**
     * Handles tool execution errors.
     * 
     * @param tool the tool that failed
     * @param error the error that occurred
     * @return the error result
     */
    IToolResult handleToolError(ITool tool, Exception error);
    
    /**
     * Gets the execution status of a tool.
     * 
     * @param toolId the tool ID
     * @return the execution status
     */
    String getToolExecutionStatus(String toolId);
    
    /**
     * Gets the execution history for a tool.
     * 
     * @param toolId the tool ID
     * @return the execution history
     */
    java.util.List<IToolResult> getToolExecutionHistory(String toolId);
    
    /**
     * Gets the execution statistics for a tool.
     * 
     * @param toolId the tool ID
     * @return the execution statistics
     */
    Map<String, Object> getToolExecutionStatistics(String toolId);
    
    /**
     * Cancels a running tool execution.
     * 
     * @param toolId the tool ID
     * @param sessionId the session ID
     * @return true if cancellation was successful, false otherwise
     */
    boolean cancelToolExecution(String toolId, String sessionId);
    
    /**
     * Checks if a tool is currently executing.
     * 
     * @param toolId the tool ID
     * @param sessionId the session ID
     * @return true if tool is executing, false otherwise
     */
    boolean isToolExecuting(String toolId, String sessionId);
    
    /**
     * Sets the timeout for tool execution.
     * 
     * @param timeoutMs the timeout in milliseconds
     */
    void setExecutionTimeout(long timeoutMs);
    
    /**
     * Gets the current execution timeout.
     * 
     * @return the timeout in milliseconds
     */
    long getExecutionTimeout();
}
