package com.api.whatsapp.repository;

import com.api.whatsapp.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Chatroom findByChatroomId(Long chatroomId);
}