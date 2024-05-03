package com.api.whatsapp.controller;

import com.api.whatsapp.model.Message;
import com.api.whatsapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<Page<Message>> getMessages(Pageable pageable, @RequestParam(value = "chatroomId", required = false) Long chatroomId, @RequestParam(value = "search", required = false) String search) {
        Page<Message> messages;
        if (chatroomId != null) {
        }

    }
}