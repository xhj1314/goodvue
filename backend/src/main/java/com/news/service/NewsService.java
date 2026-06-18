package com.news.service;

import com.news.dto.NewsRequest;
import com.news.entity.News;
import com.news.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    // ── 获取全部 / 关键字搜索 ─────────────────────────────────────────
    public List<News> getAll(String keyword) {
        return newsRepository.searchByKeyword(keyword);
    }

    // ── 获取单条新闻 ──────────────────────────────────────────────────
    public Optional<News> getById(Long id) {
        return newsRepository.findById(id);
    }

    // ── 发布新闻 ──────────────────────────────────────────────────────
    public News publish(NewsRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (req.getContent() == null || req.getContent().isBlank()) {
            throw new IllegalArgumentException("正文不能为空");
        }
        News news = new News(req.getTitle(), req.getContent(), LocalDateTime.now());
        return newsRepository.save(news);
    }
}
