package com.arv.framework.adk.interfaces.session;

import java.util.Map;

/**
 * Individual message in a conversation.
 * This interface defines the structure of messages in the ConnectADK framework.
 */
public interface IMessage {
    
    /**
     * Gets the unique message ID.
     * 
     * @return the message ID
     */
    String getId();
    
    /**
     * Gets the content of the message.
     * 
     * @return the message content
     */
    String getContent();
    
    /**
     * Sets the content of the message.
     * 
     * @param content the message content
     */
    void setContent(String content);
    
    /**
     * Gets the sender of the message.
     * 
     * @return the sender
     */
    String getSender();
    
    /**
     * Sets the sender of the message.
     * 
     * @param sender the sender
     */
    void setSender(String sender);
    
    /**
     * Gets the timestamp when the message was created.
     * 
     * @return the creation timestamp
     */
    long getTimestamp();
    
    /**
     * Sets the timestamp of the message.
     * 
     * @param timestamp the timestamp
     */
    void setTimestamp(long timestamp);
    
    /**
     * Gets the message type.
     * 
     * @return the message type
     */
    String getType();
    
    /**
     * Sets the message type.
     * 
     * @param type the message type
     */
    void setType(String type);
    
    /**
     * Gets the metadata associated with the message.
     * 
     * @return the metadata map
     */
    Map<String, Object> getMetadata();
    
    /**
     * Sets the metadata for the message.
     * 
     * @param metadata the metadata map
     */
    void setMetadata(Map<String, Object> metadata);
    
    /**
     * Gets a specific metadata value by key.
     * 
     * @param key the metadata key
     * @return the metadata value
     */
    Object getMetadataValue(String key);
    
    /**
     * Sets a specific metadata value.
     * 
     * @param key the metadata key
     * @param value the metadata value
     */
    void setMetadataValue(String key, Object value);
    
    /**
     * Checks if the message is from the user.
     * 
     * @return true if from user, false otherwise
     */
    boolean isFromUser();
    
    /**
     * Checks if the message is from the agent.
     * 
     * @return true if from agent, false otherwise
     */
    boolean isFromAgent();
    
    /**
     * Gets the message priority.
     * 
     * @return the priority level
     */
    int getPriority();
    
    /**
     * Sets the message priority.
     * 
     * @param priority the priority level
     */
    void setPriority(int priority);
    
    /**
     * Checks if the message has been processed.
     * 
     * @return true if processed, false otherwise
     */
    boolean isProcessed();
    
    /**
     * Marks the message as processed.
     */
    void markAsProcessed();
    
    /**
     * Gets the message length.
     * 
     * @return the message length
     */
    int getLength();
}
