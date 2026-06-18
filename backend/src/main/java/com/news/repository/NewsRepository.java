package com.news.repository;

import com.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * 按标题或正文模糊搜索（keyword 为 null 或空时返回全部）
     */
    @Query("SELECT n FROM News n WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY n.creation DESC")
    List<News> searchByKeyword(@Param("keyword") String keyword);
}
