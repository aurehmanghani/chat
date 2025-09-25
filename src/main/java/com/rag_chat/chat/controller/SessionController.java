package com.rag_chat.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rag_chat.chat.dto.PaginationResponse;
import com.rag_chat.chat.dto.SessionDto;
import com.rag_chat.chat.entity.ChatSession;
import com.rag_chat.chat.service.SessionService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
@Validated
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SessionDto.CreateRequest req) {
        ChatSession created = sessionService.create(req.getUserId(), req.getTitle());
        return ResponseEntity.status(201).body(new SessionDto.Response(created));
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam("user_id") @NotBlank String userId,
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "limit", defaultValue = "50") @Min(1) @Max(100) int limit,
            @RequestParam(value = "include_deleted", defaultValue = "false") boolean includeDeleted
    ) {
        Page<ChatSession> p = sessionService.findByUserId(userId, page, limit, includeDeleted);
        PaginationResponse pr = new PaginationResponse(page, limit, p.getTotalElements(), p.getTotalPages());
        return ResponseEntity.ok().body(new Object() {
            public final boolean success = true;
            public final String message = "Sessions retrieved successfully";
            public final Object data = p.stream().map(SessionDto.Response::new).collect(Collectors.toList());
            public final PaginationResponse pagination = pr;
        });
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> get(
            @PathVariable("sessionId") UUID sessionId,
            @RequestParam("user_id") @NotBlank String userId
    ) {
        ChatSession s = sessionService.findById(sessionId, userId);
        return ResponseEntity.ok().body(new SessionDto.Response(s));
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<?> patch(
            @PathVariable("sessionId") UUID sessionId,
            @Valid @RequestBody SessionDto.UpdateRequest req
    ) {
        ChatSession updated = sessionService.update(sessionId, req.getUserId(), req.getTitle(), req.getIsFavorite());
        return ResponseEntity.ok().body(new SessionDto.Response(updated));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> delete(
            @PathVariable("sessionId") UUID sessionId,
            @RequestParam("user_id") @NotBlank String userId,
            @RequestParam(value = "hard_delete", defaultValue = "false") boolean hardDelete
    ) {
        if (hardDelete) {
            sessionService.hardDelete(sessionId, userId);
            return ResponseEntity.ok().body(new Object() {
                public final boolean success = true;
                public final String message = "Session permanently deleted successfully";
            });
        } else {
            ChatSession s = sessionService.softDelete(sessionId, userId);
            return ResponseEntity.ok().body(new Object() {
                public final boolean success = true;
                public final String message = "Session deleted successfully";
                public final Object data = new SessionDto.Response(s);
            });
        }
    }
}
