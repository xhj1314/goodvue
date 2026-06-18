-- 设置客户端字符集为 UTF-8，避免中文乱码
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS newsdb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE newsdb;

CREATE TABLE IF NOT EXISTS `user` (
  `user_id`  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email`    VARCHAR(50)  NOT NULL UNIQUE          COMMENT '邮箱',
  `username` VARCHAR(20)  NOT NULL UNIQUE          COMMENT '用户名',
  `password` VARCHAR(100) NULL                     COMMENT '密码',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `news` (
  `news_id`  BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `title`    VARCHAR(255)  NULL DEFAULT NULL        COMMENT '新闻标题',
  `content`  LONGTEXT      NULL                     COMMENT '新闻正文',
  `creation` DATETIME(6)   NULL DEFAULT NULL        COMMENT '创建时间',
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `news` (`title`, `content`, `creation`) VALUES
('欢迎使用新闻管理系统', '## 欢迎使用\n\n这是一条示例新闻，支持 Markdown 格式编写。\n\n- 支持列表\n- 支持 **粗体**\n- 支持 *斜体*\n\n> 引用文字示例\n\n```\n代码块示例\n```', NOW()),
('系统功能介绍', '## 功能一览\n\n1. 用户注册与登录\n2. 新闻浏览与搜索\n3. 后台新闻发布（需登录）\n\n本系统采用 Spring Boot + Vue 前后端分离架构。', NOW());
