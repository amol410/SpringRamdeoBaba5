package com.demo.empmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public RefreshToken() {}
    //
    // public RefreshToken(Long id, String token, Instant expiryDate, User user) { ... }
    //
    // public Long getId() { return id; }
    // public void setId(Long id) { this.id = id; }
    // public String getToken() { return token; }
    // public void setToken(String token) { this.token = token; }
    // public Instant getExpiryDate() { return expiryDate; }
    // public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
    // public User getUser() { return user; }
    // public void setUser(User user) { this.user = user; }
    //
    // public static RefreshTokenBuilder builder() { return new RefreshTokenBuilder(); }
    // public static class RefreshTokenBuilder { ... }
}
