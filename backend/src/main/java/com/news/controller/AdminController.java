package com.news.controller;

import com.news.dto.NewsRequest;
import com.news.entity.News;
import com.news.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final NewsService newsService;

    public AdminController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * GET /admins/hi
     * 后台连通测试（需要 HTTP Basic 认证）
     */
    @GetMapping("/hi")
    public ResponseEntity<String> hi() {
        return ResponseEntity.ok("Hello World!");
    }

    /**
     * POST /admins/news
     * 发布新闻（需要 HTTP Basic 认证）
     */
    @PostMapping("/news")
    public ResponseEntity<?> publish(@RequestBody NewsRequest req) {
        try {
            News saved = newsService.publish(req);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "发布成功",
                    "newsId",  saved.getNewsId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
