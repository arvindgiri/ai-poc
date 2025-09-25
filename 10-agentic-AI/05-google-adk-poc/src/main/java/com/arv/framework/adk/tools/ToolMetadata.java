package com.arv.framework.adk.tools;

import com.arv.framework.adk.interfaces.enums.ReturnType;
import com.arv.framework.adk.interfaces.enums.ToolCategory;
import com.arv.framework.adk.interfaces.tool.IParameter;
import com.arv.framework.adk.interfaces.tool.IToolMetadata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of IToolMetadata for tool information.
 * Contains tool metadata like name, description, parameters, and return type.
 */
public class ToolMetadata implements IToolMetadata {
  
  private String id;
  private String name;
  private String description;
  private String version;
  private List<IParameter> parameters;
  private ReturnType returnType;
  private ToolCategory category;
  private Map<String, Object> properties;
  
  /**
   * Constructor for ToolMetadata.
   * 
   * @param id the tool ID
   * @param name the tool name
   * @param description the tool description
   */
  public ToolMetadata(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.version = "1.0.0";
    this.parameters = new ArrayList<>();
    this.returnType = ReturnType.STRING;
    this.category = ToolCategory.UTILITY;
    this.properties = new HashMap<>();
  }
  
  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public String getDescription() {
    return description;
  }
  
  @Override
  public String getVersion() {
    return version;
  }
  
  /**
   * Sets the version of the tool.
   * 
   * @param version the tool version
   */
  public void setVersion(String version) {
    this.version = version;
  }
  
  @Override
  public List<IParameter> getParameters() {
    return new ArrayList<>(parameters);
  }
  
  /**
   * Adds a parameter to the tool metadata.
   * 
   * @param parameter the parameter to add
   */
  public void addParameter(IParameter parameter) {
    this.parameters.add(parameter);
  }
  
  @Override
  public ReturnType getReturnType() {
    return returnType;
  }
  
  /**
   * Sets the return type of the tool.
   * 
   * @param returnType the return type
   */
  public void setReturnType(ReturnType returnType) {
    this.returnType = returnType;
  }
  
  @Override
  public ToolCategory getCategory() {
    return category;
  }
  
  /**
   * Sets the category of the tool.
   * 
   * @param category the tool category
   */
  public void setCategory(ToolCategory category) {
    this.category = category;
  }
  
  @Override
  public Map<String, Object> getProperties() {
    return new HashMap<>(properties);
  }
  
  @Override
  public Object getProperty(String key) {
    return properties.get(key);
  }
  
  @Override
  public void setProperty(String key, Object value) {
    properties.put(key, value);
  }
  
  @Override
  public boolean hasParameter(String parameterName) {
    return parameters.stream()
        .anyMatch(param -> param.getName().equals(parameterName));
  }
  
  @Override
  public IParameter getParameter(String parameterName) {
    return parameters.stream()
        .filter(param -> param.getName().equals(parameterName))
        .findFirst()
        .orElse(null);
  }
}
