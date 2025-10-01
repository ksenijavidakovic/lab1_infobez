package com.infobez.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    @GetMapping("/data")
    public List<Map<String, Object>> getData(Authentication auth) {
        String safeUser = HtmlUtils.htmlEscape(auth.getName());
        return List.of(
                Map.of("id", 1, "owner", safeUser, "title", "Hello"),
                Map.of("id", 2, "owner", safeUser, "title", "World")
        );
    }

    @PostMapping("/echo")
    public Map<String, String> echo(@RequestBody Map<String, String> body) {
        String msg = HtmlUtils.htmlEscape(body.getOrDefault("message", ""));
        return Map.of("echo", msg);
    }
}
