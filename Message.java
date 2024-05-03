package com.api.whatsapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    private Boolean isDelivered;
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    private String content;
    private LocalDateTime timestamp;
    private boolean delivered;
    private boolean read;
}
