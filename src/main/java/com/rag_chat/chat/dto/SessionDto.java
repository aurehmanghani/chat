package com.rag_chat.chat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.rag_chat.chat.entity.ChatSession;

public class SessionDto {

    public static class CreateRequest {
        @NotBlank(message = "user_id is required")
        private String userId;

        @Size(max = 500)
        private String title;

        // getters/setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }

    public static class UpdateRequest {
        @NotBlank
        private String userId;
        @Size(max = 500)
        private String title;
        private Boolean isFavorite;

        // getters/setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Boolean getIsFavorite() { return isFavorite; }
        public void setIsFavorite(Boolean isFavorite) { this.isFavorite = isFavorite; }
    }

    public static class Response {
        public String id;
        public String user_id;
        public String title;
        public boolean is_favorite;
        public String created_at;
        public String updated_at;

        public Response(ChatSession s) {
            this.id = s.getId().toString();
            this.user_id = s.getUserId();
            this.title = s.getTitle();
            this.is_favorite = s.isFavorite();
            this.created_at = s.getCreatedAt() != null ? s.getCreatedAt().toString() : null;
            this.updated_at = s.getUpdatedAt() != null ? s.getUpdatedAt().toString() : null;
        }
    }
}
