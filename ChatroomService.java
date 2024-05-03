package com.api.whatsapp.service;

import com.api.whatsapp.model.Chatroom;
import com.api.whatsapp.repository.ChatroomRepository;
import com.api.whatsapp.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatroomService {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastMessage(ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatMessage.getChatroomId(), chatMessage);
    }
    public Page<Chatroom> getAllChatrooms(Pageable pageable) {
        return chatroomRepository.findAll(pageable);
    }

    public Chatroom getChatroomById(Long id) {
        Optional<Chatroom> chatroomOptional = chatroomRepository.findById(id);
        return chatroomOptional.orElse(null);
    }

    public Page<Chatroom> searchChatrooms(String search, Pageable pageable) {
        // Implement search logic based on the search term
        return chatroomRepository.findAll(pageable);
    }
}
