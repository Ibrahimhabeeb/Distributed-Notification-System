package com.hng.user_service.models;
import jakarta.persistence.*;
import lombok.*;
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

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"))
//    @Column(name="role")
//    private Set<String> roles;

    private String pushToken;

    @Column(columnDefinition = "jsonb")
    private Map<String, Object> preferences;

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