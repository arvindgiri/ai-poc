package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.core.IToolRegistry;
import com.arv.framework.adk.interfaces.tool.ITool;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple implementation of IToolRegistry.
 */
public class SimpleToolRegistry implements IToolRegistry {
  
  private final Map<String, ITool> tools = new ConcurrentHashMap<>();
  
  @Override
  public boolean registerTool(ITool tool) {
    if (tool == null || tool.getId() == null) {
      return false;
    }
    tools.put(tool.getId(), tool);
    return true;
  }
  
  @Override
  public boolean unregisterTool(String toolId) {
    return tools.remove(toolId) != null;
  }
  
  @Override
  public Optional<ITool> getTool(String toolId) {
    return Optional.ofNullable(tools.get(toolId));
  }
  
  // Helper method for finding tools by name (not part of interface)
  public ITool findToolByName(String toolName) {
    return tools.values().stream()
        .filter(tool -> tool.getName().equalsIgnoreCase(toolName))
        .findFirst()
        .orElse(null);
  }
  
  @Override
  public List<ITool> getAllTools() {
    return tools.values().stream().collect(Collectors.toList());
  }
  
  @Override
  public List<ITool> getEnabledTools() {
    return tools.values().stream()
        .filter(ITool::isEnabled)
        .collect(Collectors.toList());
  }
  
  @Override
  public List<ITool> getDisabledTools() {
    return tools.values().stream()
        .filter(tool -> !tool.isEnabled())
        .collect(Collectors.toList());
  }
  
  @Override
  public boolean enableTool(String toolId) {
    ITool tool = tools.get(toolId);
    if (tool != null) {
      tool.setEnabled(true);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean disableTool(String toolId) {
    ITool tool = tools.get(toolId);
    if (tool != null) {
      tool.setEnabled(false);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean isToolRegistered(String toolId) {
    return tools.containsKey(toolId);
  }
  
  @Override
  public boolean isToolEnabled(String toolId) {
    ITool tool = tools.get(toolId);
    return tool != null && tool.isEnabled();
  }
  
  @Override
  public Optional<IToolMetadata> getToolMetadata(String toolId) {
    ITool tool = tools.get(toolId);
    return tool != null ? Optional.of(tool.getMetadata()) : Optional.empty();
  }
  
  @Override
  public int getToolCount() {
    return tools.size();
  }
}
