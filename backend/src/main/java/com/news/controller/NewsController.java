package com.news.controller;

import com.news.entity.News;
import com.news.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * GET /news
     * GET /news?keyword=关键字
     * 获取新闻列表 / 关键字搜索（无需登录）
     */
    @GetMapping
    public ResponseEntity<List<News>> list(
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(newsService.getAll(keyword));
    }

    /**
     * GET /news/{id}
     * 获取单条新闻详情（无需登录）
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return newsService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("message", "新闻不存在，ID: " + id)));
    }
}
