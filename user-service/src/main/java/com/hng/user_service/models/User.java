package com.hng.user_service.models;
import com.hng.user_service.dtos.UserPreference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Type;


import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;
    private String name;
    private String pushToken;
    @Column(name = "email_pref")
    private boolean emailPref;

    @Column(name = "push_pref")
    private boolean pushPref;

    private Instant createdAt;
    private Instant updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}