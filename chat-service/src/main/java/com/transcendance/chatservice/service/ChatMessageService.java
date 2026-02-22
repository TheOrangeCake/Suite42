package com.transcendance.chatservice.service;

// 1. Imports des classes Java standards
import java.util.Date;
import java.util.List;

// 2. Imports des annotations Spring et Lombok
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

// 3. Imports de tes propres classes (Modèle et Repository)
import com.transcendance.chatservice.model.ChatMessage;
import com.transcendance.chatservice.model.MessageStatus;
import com.transcendance.chatservice.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;

    public ChatMessage save(ChatMessage message) {
        // FIX: Validation et initialisation
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du message ne peut pas être vide");
        }
        
        if (message.getSenderId() == null || message.getRecipientId() == null) {
            throw new IllegalArgumentException("senderId et recipientId sont obligatoires");
        }

        // On s'assure que la date est fixée au moment de l'enregistrement
        message.setTimestamp(new Date());
        // FIX: Initialiser le status si non défini
        if (message.getStatus() == null) {
            message.setStatus(MessageStatus.RECEIVED);
        }
        
        return repository.save(message);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        // FIX: Retourner la conversation complète (dans les deux sens)
        return repository.findConversation(senderId, recipientId);
    }
}