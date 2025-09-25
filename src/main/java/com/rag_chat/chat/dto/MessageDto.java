package com.rag_chat.chat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rag_chat.chat.entity.ChatMessage;

import java.util.UUID;

public class MessageDto {

    public static class CreateRequest {
        @NotNull
        private UUID sessionId;
        @NotBlank
        @Pattern(regexp = "user|assistant")
        private String sender;
        @NotBlank
        private String content;
        private Object context;
        private Object metadata;

        // getters/setters
        public UUID getSessionId() { return sessionId; }
        public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Object getContext() { return context; }
        public void setContext(Object context) { this.context = context; }
        public Object getMetadata() { return metadata; }
        public void setMetadata(Object metadata) { this.metadata = metadata; }
    }

    public static class UpdateRequest {
        @NotBlank
        private String userId;
        private String content;
        private Object context;
        private Object metadata;

        // getters/setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Object getContext() { return context; }
        public void setContext(Object context) { this.context = context; }
        public Object getMetadata() { return metadata; }
        public void setMetadata(Object metadata) { this.metadata = metadata; }
    }

    public static class Response {
        public String id;
        public String session_id;
        public String sender;
        public String content;
        public Object context;
        public Object metadata;
        public String created_at;

        public Response(ChatMessage m) {
            this.id = m.getId().toString();
            this.session_id = m.getSession().getId().toString();
            this.sender = m.getSender();
            this.content = m.getContent();
            try {
                this.context = m.getContext() != null ? new com.fasterxml.jackson.databind.ObjectMapper().readTree(m.getContext()) : null;
                this.metadata = m.getMetadata() != null ? new com.fasterxml.jackson.databind.ObjectMapper().readTree(m.getMetadata()) : null;
            } catch (Exception e) {
                this.context = null;
                this.metadata = null;
            }
            this.created_at = m.getCreatedAt() != null ? m.getCreatedAt().toString() : null;
        }
    }
}

