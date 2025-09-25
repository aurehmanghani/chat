package com.rag_chat.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rag_chat.chat.entity.ChatSession;

import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    Page<ChatSession> findAllByUserIdAndDeletedAtIsNullOrderByIsFavoriteDescUpdatedAtDesc(String userId, Pageable pageable);
    Page<ChatSession> findAllByUserId(String userId, Pageable pageable);
}

