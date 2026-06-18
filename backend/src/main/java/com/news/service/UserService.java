package com.news.service;

import com.news.dto.LoginRequest;
import com.news.dto.RegisterRequest;
import com.news.entity.User;
import com.news.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ── Spring Security 使用，加载用户供 HTTP Basic 验证 ──────────────
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    // ── 注册 ──────────────────────────────────────────────────────────
    public Map<String, Object> register(RegisterRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank() ||
            req.getUsername() == null || req.getUsername().isBlank() ||
            req.getPassword() == null || req.getPassword().isBlank()) {
            return error("邮箱、用户名、密码不能为空");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return error("邮箱已被注册");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            return error("用户名已存在");
        }
        User user = new User(req.getEmail(), req.getUsername(),
                             passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
        return ok("注册成功");
    }

    // ── 登录（自定义验证，返回用户信息供前端保存） ────────────────────
    public Map<String, Object> login(LoginRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            return error("用户名和密码不能为空");
        }
        Optional<User> opt = userRepository.findByUsername(req.getUsername());
        if (opt.isEmpty()) {
            return error("用户名或密码错误");
        }
        User user = opt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return error("用户名或密码错误");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        return result;
    }

    // ── 获取当前用户（HTTP Basic 已验证，从 Security 上下文取用户名） ──
    public Map<String, Object> getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .map(u -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("userId",   u.getUserId());
                    m.put("username", u.getUsername());
                    m.put("email",    u.getEmail());
                    return m;
                })
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

    // ── Helper ───────────────────────────────────────────────────────
    private Map<String, Object> ok(String msg) {
        return Map.of("success", true,  "message", msg);
    }
    private Map<String, Object> error(String msg) {
        return Map.of("success", false, "message", msg);
    }
}
