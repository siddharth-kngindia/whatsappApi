package com.api.whatsapp.repository;


import com.api.whatsapp.model.Message;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByMessageId(Long messageId);
}