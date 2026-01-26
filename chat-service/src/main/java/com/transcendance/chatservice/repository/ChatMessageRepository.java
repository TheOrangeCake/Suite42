package com.transcendance.chatservice.repository;

import com.transcendance.chatservice.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Cette méthode magique permet de récupérer toute la conversation entre deux personnes
    // Spring Data JPA va comprendre tout seul la requête SQL à générer !
    List<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipientId);

    // Pour récupérer l'historique complet (Alice vers Bob ET Bob vers Alice)
    List<ChatMessage> findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
        String s1, String r1, String s2, String r2
    );
}