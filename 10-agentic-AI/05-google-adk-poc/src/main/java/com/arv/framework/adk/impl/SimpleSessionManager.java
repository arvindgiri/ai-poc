package com.arv.framework.adk.impl;

import com.arv.framework.adk.interfaces.core.ISessionManager;
import com.arv.framework.adk.interfaces.session.ISession;
import com.arv.framework.adk.interfaces.session.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple implementation of ISessionManager.
 */
public class SimpleSessionManager implements ISessionManager {

  private final Map<String, ISession> sessions = new ConcurrentHashMap<>();

  @Override
  public ISession createSession(String sessionId) {
    SimpleSession session = new SimpleSession(sessionId);
    sessions.put(sessionId, session);
    return session;
  }

  @Override
  public ISession createSession(String sessionId, String userId) {
    SimpleSession session = new SimpleSession(sessionId, userId);
    sessions.put(sessionId, session);
    return session;
  }

  @Override
  public Optional<ISession> getSession(String sessionId) {
    return Optional.ofNullable(sessions.get(sessionId));
  }

  @Override
  public boolean endSession(String sessionId) {
    ISession session = sessions.get(sessionId);
    if (session != null) {
      session.deactivate();
      return sessions.remove(sessionId) != null;
    }
    return false;
  }

  @Override
  public boolean isSessionActive(String sessionId) {
    ISession session = sessions.get(sessionId);
    return session != null && session.isActive();
  }

  @Override
  public boolean updateSessionContext(String sessionId, Map<String, Object> context) {
    ISession session = sessions.get(sessionId);
    if (session != null) {
      session.updateContext(context);
      return true;
    }
    return false;
  }

  @Override
  public Map<String, Object> getSessionContext(String sessionId) {
    ISession session = sessions.get(sessionId);
    return session != null ? session.getContext() : Map.of();
  }

  @Override
  public boolean addMessage(String sessionId, IMessage message) {
    ISession session = sessions.get(sessionId);
    if (session != null && message != null) {
      session.addMessage(message);
      return true;
    }
    return false;
  }

  @Override
  public List<IMessage> getSessionHistory(String sessionId) {
    ISession session = sessions.get(sessionId);
    return session != null ? session.getMessageHistory() : List.of();
  }

  @Override
  public List<IMessage> getRecentMessages(String sessionId, int count) {
    ISession session = sessions.get(sessionId);
    if (session != null) {
      List<IMessage> messages = session.getMessageHistory();
      int size = messages.size();
      if (count >= size) {
        return messages;
      }
      return messages.subList(size - count, size);
    }
    return List.of();
  }

  @Override
  public boolean clearHistory(String sessionId) {
    ISession session = sessions.get(sessionId);
    if (session != null) {
      session.clearHistory();
      return true;
    }
    return false;
  }

  @Override
  public int getMessageCount(String sessionId) {
    ISession session = sessions.get(sessionId);
    return session != null ? session.getMessageCount() : 0;
  }

  @Override
  public List<ISession> getActiveSessions() {
    return sessions.values().stream()
        .filter(ISession::isActive)
        .collect(Collectors.toList());
  }

  @Override
  public int cleanupExpiredSessions() {
    // Simple cleanup: remove sessions that are not active
    List<String> expiredSessions = sessions.entrySet().stream()
        .filter(entry -> !entry.getValue().isActive())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
    
    for (String sessionId : expiredSessions) {
      sessions.remove(sessionId);
    }
    
    return expiredSessions.size();
  }
}