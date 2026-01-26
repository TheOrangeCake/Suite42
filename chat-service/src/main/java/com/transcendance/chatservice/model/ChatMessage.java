package com.transcendance.chatservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity               // Dit à Spring que c'est une table en base de données
@Table(name = "messages")
@Data                 // Génère les getters, setters, toString (Lombok)
@NoArgsConstructor    // Génère un constructeur vide (obligatoire pour JPA)
@AllArgsConstructor   // Génère un constructeur avec tous les champs
@Builder              // Permet de créer l'objet plus facilement
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;    // Le pseudo ou l'ID de celui qui envoie
    private String recipientId; // Le pseudo ou l'ID de celui qui reçoit
    private String content;     // Le texte du message

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;     // L'heure d'envoi

    private MessageStatus status; // Reçu, Lu, etc. (Optionnel)
}

enum MessageStatus {
    RECEIVED, DELIVERED
}