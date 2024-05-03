package com.api.whatsapp.service;

import com.api.whatsapp.model.Chatroom;
import com.api.whatsapp.model.Message;
import com.api.whatsapp.model.Profile;
import com.api.whatsapp.repository.MessageRepository;
import com.api.whatsapp.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public void markMessageDelivered(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new EntityNotFoundException("Message not found"));
        message.setDeliveredAt(LocalDateTime.now());
        message.setIsDelivered(true);
        messageRepository.save(message);
    }

    public void markMessageRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new EntityNotFoundException("Message not found"));
        message.setReadAt(LocalDateTime.now());
        message.setIsRead(true);
        messageRepository.save(message);
    }

    public Page<Message> getMessages(Pageable pageable, @RequestParam(value = "chatroomId", required = false) Long chatroomId, @RequestParam(value = "search", required = false) String search) {
        Optional<Chatroom> chatroomOptional = chatroomRepository.findById(chatroomId);
        Chatroom chatroom = chatroomOptional.orElse(null);

        // Implement search logic based on the search term
        return messageRepository.findAll(pageable);
    }

    public Message saveMessage(Message message) {
        Optional<Profile> senderOptional = profileRepository.findById(message.getSender().getId());
        Profile sender = senderOptional.orElse(null);
        message.setSender(sender);
        return messageRepository.save(message);
    }
}
