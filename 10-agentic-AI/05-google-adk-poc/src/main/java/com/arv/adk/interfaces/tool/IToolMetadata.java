package com.arv.adk.interfaces.tool;

import com.arv.adk.interfaces.enums.ToolCategory;
import com.arv.adk.interfaces.enums.ReturnType;
import java.util.List;
import java.util.Map;

/**
 * Contains tool information like name, description, parameters.
 * This interface defines the metadata structure for tools in the ConnectADK framework.
 */
public interface IToolMetadata {
    
    /**
     * Gets the unique identifier of the tool.
     * 
     * @return the tool ID
     */
    String getId();
    
    /**
     * Gets the name of the tool.
     * 
     * @return the tool name
     */
    String getName();
    
    /**
     * Gets the description of the tool.
     * 
     * @return the tool description
     */
    String getDescription();
    
    /**
     * Gets the version of the tool.
     * 
     * @return the tool version
     */
    String getVersion();
    
    /**
     * Gets the list of parameters for the tool.
     * 
     * @return the list of parameters
     */
    List<IParameter> getParameters();
    
    /**
     * Gets the return type of the tool.
     * 
     * @return the return type
     */
    ReturnType getReturnType();
    
    /**
     * Gets the category of the tool.
     * 
     * @return the tool category
     */
    ToolCategory getCategory();
    
    /**
     * Gets additional properties of the tool.
     * 
     * @return the properties map
     */
    Map<String, Object> getProperties();
    
    /**
     * Gets a specific property by key.
     * 
     * @param key the property key
     * @return the property value
     */
    Object getProperty(String key);
    
    /**
     * Sets a property.
     * 
     * @param key the property key
     * @param value the property value
     */
    void setProperty(String key, Object value);
    
    /**
     * Checks if the tool has a specific parameter.
     * 
     * @param parameterName the parameter name
     * @return true if parameter exists, false otherwise
     */
    boolean hasParameter(String parameterName);
    
    /**
     * Gets a parameter by name.
     * 
     * @param parameterName the parameter name
     * @return the parameter if found, null otherwise
     */
    IParameter getParameter(String parameterName);
}
