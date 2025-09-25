package com.rag_chat.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rag_chat.chat.entity.ChatMessage;
import com.rag_chat.chat.entity.ChatSession;
import com.rag_chat.chat.exception.ResourceNotFoundException;
import com.rag_chat.chat.repository.ChatMessageRepository;
import com.rag_chat.chat.repository.ChatSessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private ChatSessionRepository sessionRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public ChatMessage create(UUID sessionId, String sender, String content, Object context, Object metadata) {
        ChatSession session = sessionRepository.findById(sessionId)
                .filter(s -> s.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        ChatMessage m = new ChatMessage();
        m.setSession(session);
        m.setSender(sender);
        m.setContent(content);
        try {
            m.setContext(context != null ? mapper.writeValueAsString(context) : null);
            m.setMetadata(metadata != null ? mapper.writeValueAsString(metadata) : null);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid JSON for context/metadata");
        }
        ChatMessage saved = messageRepository.save(m);

        // update session timestamp
        session.setUpdatedAt(java.time.OffsetDateTime.now());
        sessionRepository.save(session);

        return saved;
    }

    public Page<ChatMessage> findBySession(UUID sessionId, int page, int size, String userId) {
        ChatSession session = sessionRepository.findById(sessionId)
                .filter(s -> s.getDeletedAt() == null)
                .filter(s -> userId == null || s.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").ascending());
        return messageRepository.findBySessionOrderByCreatedAtAsc(session, pageable);
    }

    public ChatMessage findById(UUID messageId, String userId) {
        Optional<ChatMessage> opt = messageRepository.findById(messageId);
        ChatMessage m = opt.orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        if (m.getSession().getDeletedAt() != null) throw new ResourceNotFoundException("Message not found");
        if (userId != null && !m.getSession().getUserId().equals(userId)) throw new ResourceNotFoundException("Message not found");
        return m;
    }

    @Transactional
    public ChatMessage update(UUID messageId, String userId, String content, Object context, Object metadata) {
        ChatMessage m = findById(messageId, userId);
        if (content != null) m.setContent(content);
        try {
            if (context != null) m.setContext(mapper.writeValueAsString(context));
            if (metadata != null) m.setMetadata(mapper.writeValueAsString(metadata));
        } catch (Exception ex) {
            throw new RuntimeException("Invalid JSON for context/metadata");
        }
        return messageRepository.save(m);
    }

    @Transactional
    public void delete(UUID messageId, String userId) {
        ChatMessage m = findById(messageId, userId);
        messageRepository.delete(m);
    }
}

