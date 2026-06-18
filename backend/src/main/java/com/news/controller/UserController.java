package com.news.controller;

import com.news.dto.LoginRequest;
import com.news.dto.RegisterRequest;
import com.news.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /users/register
     * 用户注册（无需登录）
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        Map<String, Object> result = userService.register(req);
        boolean success = Boolean.TRUE.equals(result.get("success"));
        return success
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    /**
     * POST /users/login
     * 用户登录（自定义验证，无需 Spring Security 拦截）
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest req) {
        Map<String, Object> result = userService.login(req);
        boolean success = Boolean.TRUE.equals(result.get("success"));
        return success
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(401).body(result);
    }

    /**
     * GET /users/me
     * 获取当前登录用户信息（需要 HTTP Basic 认证）
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication auth) {
        return ResponseEntity.ok(userService.getCurrentUser(auth.getName()));
    }
}
