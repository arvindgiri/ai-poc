package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.session.ISession;
import com.arv.framework.adk.interfaces.session.IMessage;
import com.arv.framework.adk.interfaces.enums.SessionState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple implementation of ISession.
 */
public class SimpleSession implements ISession {
  
  private final String sessionId;
  private String userId;
  private SessionState state;
  private final List<IMessage> messages;
  private final Map<String, Object> context;
  private final Map<String, Object> metadata;
  private long createdAt;
  private long lastActivity;
  
  public SimpleSession(String sessionId) {
    this(sessionId, null);
  }
  
  public SimpleSession(String sessionId, String userId) {
    this.sessionId = sessionId;
    this.userId = userId;
    this.state = SessionState.ACTIVE;
    this.messages = new CopyOnWriteArrayList<>();
    this.context = new ConcurrentHashMap<>();
    this.metadata = new ConcurrentHashMap<>();
    this.createdAt = System.currentTimeMillis();
    this.lastActivity = this.createdAt;
  }
  
  @Override
  public String getId() {
    return sessionId;
  }
  
  @Override
  public String getUserId() {
    return userId;
  }
  
  @Override
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  @Override
  public SessionState getState() {
    return state;
  }
  
  @Override
  public void setState(SessionState state) {
    this.state = state;
  }
  
  @Override
  public List<IMessage> getMessageHistory() {
    return List.copyOf(messages);
  }
  
  @Override
  public void addMessage(IMessage message) {
    if (message != null) {
      messages.add(message);
      updateLastActivity();
    }
  }
  
  @Override
  public int getMessageCount() {
    return messages.size();
  }
  
  @Override
  public void clearHistory() {
    messages.clear();
  }
  
  @Override
  public Map<String, Object> getContext() {
    return Map.copyOf(context);
  }
  
  @Override
  public void setContext(Map<String, Object> context) {
    this.context.clear();
    if (context != null) {
      this.context.putAll(context);
    }
  }
  
  @Override
  public void updateContext(Map<String, Object> updates) {
    if (updates != null) {
      this.context.putAll(updates);
    }
  }
  
  @Override
  public Object getContextValue(String key) {
    return context.get(key);
  }
  
  @Override
  public void setContextValue(String key, Object value) {
    if (key != null) {
      if (value != null) {
        context.put(key, value);
      } else {
        context.remove(key);
      }
    }
  }
  
  @Override
  public long getCreatedAt() {
    return createdAt;
  }
  
  @Override
  public long getLastActivityAt() {
    return lastActivity;
  }
  
  @Override
  public void updateLastActivity() {
    this.lastActivity = System.currentTimeMillis();
  }
  
  @Override
  public boolean isActive() {
    return this.state == SessionState.ACTIVE;
  }
  
  @Override
  public void activate() {
    setState(SessionState.ACTIVE);
    updateLastActivity();
  }
  
  @Override
  public void deactivate() {
    setState(SessionState.TERMINATED);
    updateLastActivity();
  }
  
  @Override
  public Map<String, Object> getMetadata() {
    return Map.copyOf(metadata);
  }
  
  @Override
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata.clear();
    if (metadata != null) {
      this.metadata.putAll(metadata);
    }
  }
}
