package com.transcendance.chatservice.service;

// 1. Imports des classes Java standards
import java.util.Date;
import java.util.List;

// 2. Imports des annotations Spring et Lombok
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

// 3. Imports de tes propres classes (Modèle et Repository)
import com.transcendance.chatservice.model.ChatMessage;
import com.transcendance.chatservice.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;

    public ChatMessage save(ChatMessage message) {
        // On s'assure que la date est fixée au moment de l'enregistrement
        message.setTimestamp(new Date());
        return repository.save(message);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        return repository.findBySenderIdAndRecipientId(senderId, recipientId);
    }
}