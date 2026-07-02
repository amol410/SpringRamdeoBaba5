package com.demo.empmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Generates: getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Generates: no-args constructor
@AllArgsConstructor     // Generates: all-args constructor
public class AuthRequest {
    private String username;
    private String password;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public AuthRequest() {}
    //
    // public AuthRequest(String username, String password) {
    //     this.username = username;
    //     this.password = password;
    // }
    //
    // public String getUsername() { return username; }
    // public void setUsername(String username) { this.username = username; }
    // public String getPassword() { return password; }
    // public void setPassword(String password) { this.password = password; }
}
