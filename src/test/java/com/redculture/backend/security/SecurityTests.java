package com.redculture.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityTests {

    @Test
    public void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String encoded = encoder.encode(password);
        
        System.out.println("Encoded: " + encoded);
        assertTrue(encoder.matches(password, encoded));
    }
}
