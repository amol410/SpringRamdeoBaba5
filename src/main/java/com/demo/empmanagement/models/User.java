package com.demo.empmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // =====================================================================
    // LOMBOK @Data + @Builder + @NoArgsConstructor + @AllArgsConstructor
    // replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public User() {}
    //
    // public User(Long id, String username, String password, String role) {
    //     this.id = id;
    //     this.username = username;
    //     this.password = password;
    //     this.role = role;
    // }
    //
    // public Long getId() { return id; }
    // public void setId(Long id) { this.id = id; }
    // public String getUsername() { return username; }
    // public void setUsername(String username) { this.username = username; }
    // public String getPassword() { return password; }
    // public void setPassword(String password) { this.password = password; }
    // public String getRole() { return role; }
    // public void setRole(String role) { this.role = role; }
    //
    // public static UserBuilder builder() { return new UserBuilder(); }
    // public static class UserBuilder {
    //     private Long id;
    //     private String username;
    //     private String password;
    //     private String role;
    //     public UserBuilder id(Long id) { this.id = id; return this; }
    //     public UserBuilder username(String username) { this.username = username; return this; }
    //     public UserBuilder password(String password) { this.password = password; return this; }
    //     public UserBuilder role(String role) { this.role = role; return this; }
    //     public User build() { return new User(id, username, password, role); }
    // }
}
