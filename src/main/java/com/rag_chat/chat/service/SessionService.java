package com.rag_chat.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.rag_chat.chat.entity.ChatSession;
import com.rag_chat.chat.exception.ResourceNotFoundException;
import com.rag_chat.chat.repository.ChatSessionRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private ChatSessionRepository sessionRepository;

    public ChatSession create(String userId, String title) {
        ChatSession s = new ChatSession();
        s.setUserId(userId);
        s.setTitle(title != null ? title : "New Chat");
        return sessionRepository.save(s);
    }

    public Page<ChatSession> findByUserId(String userId, int page, int size, boolean includeDeleted) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (includeDeleted) {
            return sessionRepository.findAllByUserId(userId, pageable);
        } else {
            return sessionRepository.findAllByUserIdAndDeletedAtIsNullOrderByIsFavoriteDescUpdatedAtDesc(userId, pageable);
        }
    }

    public ChatSession findById(UUID sessionId, String userId) {
        return sessionRepository.findById(sessionId)
                .filter(s -> s.getDeletedAt() == null)
                .filter(s -> userId == null || s.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    @Transactional
    public ChatSession update(UUID sessionId, String userId, String title, Boolean isFavorite) {
        ChatSession s = findById(sessionId, userId);
        if (title != null) s.setTitle(title);
        if (isFavorite != null) s.setFavorite(isFavorite);
        return sessionRepository.save(s);
    }

    @Transactional
    public ChatSession softDelete(UUID sessionId, String userId) {
        ChatSession s = findById(sessionId, userId);
        s.setDeletedAt(java.time.OffsetDateTime.now());
        return sessionRepository.save(s);
    }

    @Transactional
    public void hardDelete(UUID sessionId, String userId) {
        ChatSession s = findById(sessionId, userId);
        sessionRepository.delete(s);
    }
}

