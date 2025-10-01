package com.infobez.auth;

import com.infobez.security.JwtService;
import com.infobez.user.User;
import com.infobez.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Map<String, String> req) {
        String username = HtmlUtils.htmlEscape(req.get("username"));
        String password = req.get("password");
        if (users.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username taken"));
        }
        User u = User.builder()
                .username(username)
                .passwordHash(encoder.encode(password))
                .role("ROLE_USER")
                .build();
        users.save(u);
        return ResponseEntity.ok(Map.of("message", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");

        var user = users.findByUsername(username).orElse(null);
        if (user == null || !encoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwt.generate(user.getUsername(), Map.of("role", user.getRole()));
        return ResponseEntity.ok(Map.of("token", token, "type", "Bearer"));
    }
}
