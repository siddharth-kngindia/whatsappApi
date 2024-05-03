package com.api.whatsapp.controller;

import com.api.whatsapp.model.Chatroom;
import com.api.whatsapp.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        chatMessage.setSender(user.getUsername());
        Chatroom chatroom = chatroomService.findById(chatMessage.getChatroomId());
        chatMessage.setChatroom(chatroom);
        messageService.saveMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chatroom/{chatroomId}")
    @SendTo("/topic/chatroom/{chatroomId}")
    public ChatMessage subscribeToChatroom(@DestinationVariable Long chatroomId, SimpMessageHeaderAccessor headerAccessor) {
        User user = (User) headerAccessor.getSessionAttributes().get("user");
        Chatroom chatroom = chatroomService.findById(chatroomId);
        chatroom.getMembers().add(user);
        chatroomService.saveChatroom(chatroom);
        return new ChatMessage("You have joined the chatroom " + chatroom.getName(), user.getUsername(), chatroomId);
    }

    @GetMapping
    public ResponseEntity<Page<Chatroom>> getChatrooms(Pageable pageable, @RequestParam(value = "search", required = false) String search) {
        Page<Chatroom> chatrooms;
        if (search != null) {
            chatrooms = chatroomService.searchChatrooms(search, pageable);
        } else {
            chatrooms = chatroomService.getAllChatrooms(pageable);
        }
        return new ResponseEntity<>(chatrooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chatroom> getChatroomById(@PathVariable Long id) {
        Chatroom chatroom = chatroomService.getChatroomById(id);
        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }

    @PostMapping("/send-message")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        // Add emoji handling logic here
        String emoji = getEmojiFromMessage(chatMessage.getText());
        if (emoji != null) {
            chatMessage.setText(chatMessage.getText().replace(emoji, ""));
            chatMessage.setEmoji(emoji);
        }
        ChatMessage sentMessage = chatService.sendMessage(chatMessage);
        return ResponseEntity.ok(sentMessage);
    }

    private String getEmojiFromMessage(String message) {
        // Add emoji regex pattern here
        Pattern pattern = Pattern.compile("(\uD83C|\uD83D|\uD83E)[\\uDC00-\\uDFFF][\\uDC00-\\uDFFF]");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}