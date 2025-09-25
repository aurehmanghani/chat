package com.rag_chat.chat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rag_chat.chat.dto.MessageDto;
import com.rag_chat.chat.entity.ChatMessage;
import com.rag_chat.chat.service.MessageService;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@Validated
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MessageDto.CreateRequest req) {
        ChatMessage m = messageService.create(req.getSessionId(), req.getSender(), req.getContent(), req.getContext(), req.getMetadata());
        return ResponseEntity.status(201).body(new MessageDto.Response(m));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> listBySession(
            @PathVariable("sessionId") UUID sessionId,
            @RequestParam("user_id") @NotBlank String userId,
            @RequestParam(value = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(value = "limit", defaultValue = "100") @Min(1) @Max(500) int limit
    ) {
        Page<ChatMessage> p = messageService.findBySession(sessionId, page, limit, userId);
        return ResponseEntity.ok().body(new Object() {
            public final boolean success = true;
            public final String message = "Messages retrieved successfully";
            public final Object data = p.stream().map(MessageDto.Response::new).collect(Collectors.toList());
            public final Object pagination = new Object() {
                public final int pageNum = page;
                public final int limitNum = limit;
                public final long total = p.getTotalElements();
                public final int pages = p.getTotalPages();
            };
        });
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<?> get(@PathVariable("messageId") UUID messageId, @RequestParam("user_id") @NotBlank String userId) {
        ChatMessage m = messageService.findById(messageId, userId);
        return ResponseEntity.ok().body(new MessageDto.Response(m));
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<?> patch(@PathVariable("messageId") UUID messageId, @Valid @RequestBody MessageDto.UpdateRequest req) {
        ChatMessage updated = messageService.update(messageId, req.getUserId(), req.getContent(), req.getContext(), req.getMetadata());
        return ResponseEntity.ok().body(new MessageDto.Response(updated));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> delete(@PathVariable("messageId") UUID messageId, @RequestParam("user_id") @NotBlank String userId) {
        messageService.delete(messageId, userId);
        return ResponseEntity.ok().body(new Object() {
            public final boolean success = true;
            public final String message = "Message deleted successfully";
        });
    }
}
