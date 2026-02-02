package ru.pancoManco.weatherViewer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sessions",schema = "weather")
public class Session {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false, referencedColumnName = "id")
    private User userId;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
