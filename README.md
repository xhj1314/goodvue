# 新闻管理系统 — 项目部署与运行指南

> **项目名称**：News Management System（新闻管理系统）  
> **技术架构**：Spring Boot 3.2 + Vue 3 + MySQL 8（前后端分离）  
> **适用场景**：期末作业 / 课程设计 / 毕业设计  

---

## 目录

- [一、环境要求](#一环境要求)
- [二、安装与配置](#二安装与配置)
- [三、数据库初始化](#三数据库初始化)
- [四、启动项目](#四启动项目)
- [五、功能说明](#五功能说明)
- [六、API 接口文档](#六api-接口文档)
- [七、常见问题](#七常见问题)
- [八、项目结构说明](#八项目结构说明)

---

## 一、环境要求

| 软件 | 最低版本 | 推荐版本 | 用途 |
|------|----------|----------|------|
| **JDK (Java)** | 17+ | 21 | 运行后端 Spring Boot |
| **Node.js** | 18+ | 20+ / 24 LTS | 运行前端 Vue 项目 |
| **MySQL** | 8.0 | 8.0.36+ | 数据存储 |
| **Maven** | 3.8+ | 3.9.x | Java 项目构建工具 |

### 如何检查已安装的版本

打开终端（PowerShell 或 CMD），依次运行：

```bash
# 检查 Java 版本
java -version
# 预期输出：java version "21.0.9" （或 17+）

# 检查 Node.js 版本
node -v
# 预期输出：v24.11.1 （或 18+）

# 检查 npm 版本（随 Node.js 安装）
npm -v
# 预期输出：11.6.2

# 检查 MySQL 版本
mysql --version
# 预期输出：mysql Ver 8.0.46

# 检查 Maven 版本
mvn -version
# 预期输出：Apache Maven 3.9.11
```

> **注意**：如果 `mvn` 命令找不到，说明 Maven 未加入系统 PATH，但 **IDE（Trae/IntelliJ IDEA）内置了 Maven，不影响使用**，详见[第二章关于 Maven 的说明](#22-maven-安装与配置)。

---

## 二、安装与配置

### 2.1 JDK 安装

1. 下载地址：https://adoptium.net/
2. 选择 **Temurin JDK 21**（LTS 长期支持版）
3. 安装时记住安装路径（如 `C:\Program Files\Eclipse Adoptium\jdk-21`）
4. 安装完成后重启终端，运行 `java -version` 验证

### 2.2 Node.js 安装

1. 下载地址：https://nodejs.org/
2. 选择 **LTS 版本**（长期支持版）
3. 安装时勾选"自动添加到 PATH"
4. 安装完成后重启终端，运行 `node -v` 和 `npm -v` 验证

### 2.3 MySQL 安装与配置

#### 安装步骤

1. 下载地址：https://dev.mysql.com/downloads/installer/
2. 选择 **mysql-installer-community**（社区版，免费）
3. 安装过程中设置 **root 密码**（请牢记！后续配置需要用到）
4. 选择 **Developer Default** 开发者默认配置即可
5. 完成安装后，确保 MySQL 服务处于"正在运行"状态

#### 验证 MySQL 服务是否运行

```bash
# Windows PowerShell
Get-Service -Name "*mysql*"

# 或在 CMD 中
sc query mysql
```

预期看到 `Status: Running` 表示服务正常运行。

### 2.4 Maven 安装与配置（重点说明）

#### Maven 是什么？

Maven 是 Java 项目的**构建管理工具**，负责：
- 下载和管理项目依赖（jar 包）
- 编译 Java 代码
- 打包和运行 Spring Boot 应用

#### 关于"Maven 是否固定在 E 盘"的问题

**答案：不固定。Maven 可以放在任意位置。**

有两种使用方式：

| 方式 | 说明 | 适用场景 |
|------|------|----------|
| **方式 A：系统全局安装** | 下载 Maven 解压到任意盘符，配置系统环境变量 PATH | 命令行直接使用 `mvn` 命令 |
| **方式 B：IDE 内置/指定路径** | 不需要系统安装，由 IDE（Trae/IDEA）自己管理 Maven | 仅在 IDE 内开发使用 |

**本项目采用方式 B**——通过 IDE 配置文件指定 Maven 路径：

```json
// .vscode/settings.json（Trae IDE 配置）
{
    "maven.executable.path": "E:\\Program Files\\apache-maven-3.9.11\\bin\\mvn.cmd"
}
```

#### 如果换电脑 / 换用户 / 没有 E 盘怎么办？

只需修改 `.vscode/settings.json` 中的路径为实际的 Maven 解压位置即可：

```json
// 示例：如果解压到了 D 盘
"maven.executable.path": "D:\\tools\\apache-maven-3.9.11\\bin\\mvn.cmd"

// 示例：如果在 Mac/Linux 上
"maven.executable.path": "/usr/local/apache-maven-3.9.11/bin/mvn"
```

#### Maven 本地仓库（依赖缓存）

Maven 下载的所有依赖包（jar 文件）默认存放在：

```
C:\Users\你的用户名\.m2\repository\
```

这个路径**不受 Maven 程序位置影响**，始终在用户目录下。可以通过修改 `settings.xml` 自定义仓库位置（一般不需要改）。

#### IntelliJ IDEA 用户如何配置 Maven？

如果使用 IntelliJ IDEA 而非 Trae：

1. 打开 **File → Settings → Build, Execution, Deployment → Build Tools → Maven**
2. 在 **Maven home path** 中选择 Maven 安装目录：
   ```
   E:\Program Files\apache-maven-3.9.11
   ```
3. 点击 **Apply → OK**
4. IDEA 右侧会出现 **Maven 面板**，可以执行 Lifecycle 命令

---

## 三、数据库初始化

### 3.1 创建数据库

打开 MySQL 命令行客户端（或 Navicat/DBeaver 等 GUI 工具），执行以下 SQL：

```sql
-- 方法一：命令行方式
-- 打开 CMD 或 PowerShell，运行：
mysql -u root -p -e "source E:/2/news-system/backend/sql/newsdb.sql"
-- 输入你设置的 root 密码即可
```

或手动复制以下 SQL 到 MySQL 客户端中执行：

```sql
-- ============================================
-- 新闻管理系统 - 数据库初始化脚本
-- 数据库名：newsdb
-- 字符集：utf8mb4（支持中文 + Emoji）
-- ============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS newsdb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 使用该数据库
USE newsdb;

-- ── 用户表 ──────────────────────────────
CREATE TABLE IF NOT EXISTS `user` (
  `user_id`   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `email`     VARCHAR(50)  NOT NULL UNIQUE          COMMENT '邮箱（唯一）',
  `username`  VARCHAR(20)  NOT NULL UNIQUE          COMMENT '用户名（唯一）',
  `password`  VARCHAR(100) NULL                      COMMENT '密码（BCrypt加密）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 新闻表 ──────────────────────────────
CREATE TABLE IF NOT EXISTS `news` (
  `news_id`    BIGINT(20)     NOT NULL AUTO_INCREMENT COMMENT '新闻主键ID',
  `title`      VARCHAR(255)   NULL DEFAULT NULL        COMMENT '新闻标题',
  `content`    LONGTEXT       NULL                      COMMENT '新闻正文（Markdown格式）',
  `creation`   DATETIME(6)    NULL DEFAULT NULL        COMMENT '创建时间',
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ── 插入示例数据 ────────────────────────
INSERT INTO `news` (`title`, `content`, `creation`) VALUES 
('欢迎使用新闻管理系统', '## 欢迎使用\n\n这是一条示例新闻，支持 Markdown 格式编写。\n\n- 支持列表\n- 支持 **粗体**\n- 支持 *斜体*\n\n> 引用文字示例\n\n```\n代码块示例\n```', NOW()),
('系统功能介绍', '## 功能一览\n\n1. 用户注册与登录\n2. 新闻浏览与搜索\n3. 后台新闻发布（需登录）\n\n本系统采用 Spring Boot + Vue 前后端分离架构。', NOW());
```

### 3.2 修改数据库连接配置（重要）

根据你的实际 MySQL 密码，修改后端配置文件：

**文件位置**：`backend/src/main/resources/application.yml`

```yaml
server:
  port: 8092                    # 后端服务端口

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/newsdb?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root              # MySQL 用户名（默认 root）
    password: 你的密码           # ← 改成你自己的 MySQL root 密码！！！
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: none            # 表结构由 SQL 脚本手动创建，不自动修改
    show-sql: true              # 控制台打印 SQL（调试用）

  jackson:
    time-zone: Asia/Shanghai
    date-format: yyyy-MM-dd HH:mm:ss
```

---

## 四、启动项目

### 启动顺序（必须按此顺序）

```
第一步：启动 MySQL 服务
         ↓
第二步：启动后端（Spring Boot，端口 8092）
         ↓
第三步：启动前端（Vue + Vite，端口 5174）
         ↓
第四步：浏览器访问 http://localhost:5174
```

### 方式一：使用 Trae IDE（推荐，最简单）

#### 后端启动

1. 用 Trae 打开项目文件夹 `news-system`
2. 左侧边栏找到 **Maven 面板**（Java Projects 下方）
3. 展开 **Lifecycle**
4. 双击 **`compile`**（首次需要编译）
5. 双击 **`spring-boot:run`**（如果没有这个选项，双击 `package` 打包后再运行）
6. 看到 `Started NewsApplication in x.xxx seconds` 即启动成功

#### 前端启动

1. 打开 Trae 终端（底部面板 → Terminal）
2. 进入前端目录并安装依赖：

```bash
cd frontend
npm install
```

3. 启动前端开发服务器：

```bash
npm run dev
```

4. 看到 `Local: http://localhost:5174/` 即启动成功

### 方式二：使用命令行（通用方法）

#### 后端启动

```bash
cd backend
mvn spring-boot:run
```

#### 前端启动（新开一个终端窗口）

```bash
cd frontend
npm install      # 首次运行需要安装依赖
npm run dev
```

### 方式三：使用 IntelliJ IDEA

1. **导入项目**：File → Open → 选择 `backend` 文件夹 → 等待 Maven 自动导入
2. **配置 Maven**：Settings → Build Tools → Maven → 设置 Maven Home 路径
3. **启动后端**：右键 `NewsApplication.java` → Run 'NewsApplication'
4. **启动前端**：Terminal 中进入 frontend 目录 → `npm install && npm run dev`

### 验证启动是否成功

| 服务 | 访问地址 | 成功标志 |
|------|----------|----------|
| 后端 API | http://localhost:8092/news | 返回 JSON 数组（新闻列表） |
| 前端页面 | http://localhost:5174 | 显示登录/注册页面 |

---

## 五、功能说明

### 5.1 用户模块

| 功能 | 说明 | 接口 |
|------|------|------|
| 注册 | 填写邮箱、用户名、密码创建账号 | POST /users/register |
| 登录 | 使用用户名+密码登录（HTTP Basic 认证） | POST /users/login |
| 个人信息 | 查看当前登录用户信息 | GET /users/me |

### 5.2 新闻模块

| 功能 | 说明 | 接口 |
|------|------|------|
| 浏览列表 | 查看所有新闻（无需登录） | GET /news |
| 查看详情 | 查看 single 新闻内容（支持 Markdown 渲染） | GET /news/{id} |
| 发布新闻 | 创建新新闻（需登录） | POST /news |
| 编辑新闻 | 修改已有新闻（需登录） | PUT /news/{id} |
| 删除新闻 | 删除新闻（需登录） | DELETE /news/{id} |

### 5.3 页面路由

| 路由路径 | 页面 | 说明 |
|----------|------|------|
| `/login` | 登录页 | 用户名+密码登录 |
| `/register` | 注册页 | 新用户注册 |
| `/` 或 `/news` | 新闻列表页 | 展示所有新闻 |
| `/news/:id` | 新闻详情页 | Markdown 格式展示新闻内容 |
| `/admin` | 管理后台 | 发布/编辑/删除新闻（需登录） |

---

## 六、API 接口文档

### 公开接口（无需认证）

```
GET    /api/news              获取所有新闻列表
GET    /api/news/:id          获取单条新闻详情
POST   /api/users/register    用户注册
POST   /api/users/login       用户登录
OPTIONS /*                   CORS 预检请求
```

### 受保护接口（需要 HTTP Basic 认证）

```
GET    /api/users/me          获取当前用户信息
POST   /api/news              发布新新闻
PUT    /api/news/:id          编辑新闻
DELETE /api/news/:id          删除新闻
```

### 认证方式说明

本项目使用 **HTTP Basic Authentication** 进行身份验证：

- **请求头格式**：`Authorization: Basic Base64(用户名:密码)`
- **前端处理**：登录时将凭据保存到 localStorage，每次请求自动附加认证头
- **安全提示**：生产环境应使用 JWT Token 替代 Basic Auth

### 请求/响应示例

#### 注册请求

```http
POST /api/users/register
Content-Type: application/json

{
    "email": "test@example.com",
    "username": "testuser",
    "password": "123456"
}
```

```json
// 成功响应 (200)
{
    "success": true,
    "message": "注册成功"
}

// 失败响应 (400)
{
    "success": false,
    "message": "该用户名已被注册"
}
```

#### 登录请求

```http
POST /api/users/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456"
}
```

```json
// 成功响应 (200)
{
    "success": true,
    "message": "登录成功",
    "data": {
        "userId": 1,
        "email": "test@example.com",
        "username": "testuser"
    }
}

// 失败响应 (401)
{
    "success": false,
    "message": "用户名或密码错误"
}
```

---

## 七、常见问题

### Q1：启动后端报错 "Failed to calculate Effective POM"

**原因**：Maven 可执行文件未正确配置

**解决方法**：
1. 确保 Maven 已下载解压（见 2.4 节）
2. 在 `.vscode/settings.json` 中配置路径：
   ```json
   {
       "maven.executable.path": "E:\\Program Files\\apache-maven-3.9.11\\bin\\mvn.cmd"
   }
   ```
3. 点击 Maven 面板的 🔄 刷新按钮

### Q2：注册接口返回 401 Unauthorized

**原因**：Spring Security 默认会对所有无认证请求返回 401

**解决方法**：本项目已在 `SecurityConfig.java` 中修复此问题，确保使用最新代码重新编译启动。

### Q3：数据库连接失败 "Unsupported character encoding"

**原因**：JDBC URL 中的字符编码参数写错了

**解决方法**：确保 `application.yml` 中使用 `utf-8` 而非 `utf8mb4`：
```yaml
url: jdbc:mysql://localhost:3306/newsdb?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
```

### Q4：端口被占用

**现象**：启动时报 `Address already in use: bind`

**解决方法**：
```bash
# 查看占用端口的进程
netstat -ano | findstr :8092

# 杀掉占用进程（替换 PID 为实际进程号）
taskkill /PID 进程号 /F
```

或者修改 `application.yml` 中的端口号为其他值（如 8093）。

### Q5：前端 npm install 很慢或失败

**解决方法**：切换国内镜像源
```bash
npm config set registry https://registry.npmmirror.com
npm install
```

### Q6：Maven 依赖下载慢

**解决方法**：在 `~/.m2/settings.xml` 中配置阿里云镜像：
```xml
<mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Aliyun Maven</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

---

## 八、项目结构说明

### 整体目录结构

```
news-system/                      # 项目根目录
├── backend/                      # 后端项目（Spring Boot）
│   ├── pom.xml                   # Maven 项目配置文件
│   ├── src/main/java/com/news/
│   │   ├── NewsApplication.java  # 主启动类
│   │   ├── config/
│   │   │   └── SecurityConfig.java    # Spring Security 安全配置
│   │   ├── controller/             # 控制器层（接收 HTTP 请求）
│   │   │   ├── AdminController.java  # 管理员控制器
│   │   │   ├── NewsController.java  # 新闻控制器
│   │   │   └── UserController.java  # 用户控制器
│   │   ├── dto/                     # 数据传输对象
│   │   │   ├── LoginRequest.java    # 登录请求体
│   │   │   ├── RegisterRequest.java # 注册请求体
│   │   │   └── NewsRequest.java    # 新闻请求体
│   │   ├── entity/                  # 实体类（对应数据库表）
│   │   │   ├── User.java           # 用户实体
│   │   │   └── News.java           # 新闻实体
│   │   ├── repository/              # 数据访问层（JPA Repository）
│   │   │   ├── UserRepository.java  # 用户数据操作
│   │   │   └── NewsRepository.java  # 新闻数据操作
│   │   └── service/                 # 业务逻辑层
│   │       ├── UserService.java     # 用户业务逻辑
│   │       └── NewsService.java     # 新闻业务逻辑
│   ├── src/main/resources/
│   │   ├── application.yml          # 应用配置文件（数据库、端口等）
│   │   └── sql/
│   │       └── newsdb.sql           # 数据库初始化脚本
│   └── target/                      # 编译输出目录
│
├── frontend/                     # 前端项目（Vue 3 + TypeScript）
│   ├── package.json              # Node.js 项目配置及依赖声明
│   ├── vite.config.ts            # Vite 构建工具配置（含代理转发）
│   ├── tsconfig.json             # TypeScript 配置
│   ├── index.html                # HTML 入口文件
│   └── src/
│       ├── main.ts               # 应用入口
│       ├── App.vue               # 根组件
│       ├── auth.ts               # 认证状态管理（localStorage）
│       ├── news.ts               # 新闻相关 API 封装
│       ├── router/index.ts       # 路由配置
│       ├── style.css             # 全局样式
│       └── components/           # 页面组件
│           ├── Login.vue         # 登录页面
│           ├── Register.vue      # 注册页面
│           ├── NewsList.vue      # 新闻列表页
│           ├── NewsDetail.vue    # 新闻详情页
│           └── Admin.vue         # 管理后台页面
│
├── .vscode/
│   └── settings.json             # Trae/VS Code 编辑器配置
│                                  # 含 Maven 路径、Java 配置等
└── README.md                     # 项目说明文档（本文件）
```

### 技术栈详解

#### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 3.2.5 | 主框架，简化 Spring 开发 |
| **Spring Data JPA** | 3.2.5 | ORM 框架，操作数据库 |
| **Spring Security** | 6.2.4 | 认证与授权（HTTP Basic） |
| **Hibernate** | 6.4.4 | JPA 实现，自动生成 SQL |
| **MySQL Connector** | 8.3.0 | MySQL JDBC 驱动 |
| **Jackson** | 2.15.4 | JSON 序列化/反序列化 |
| **Tomcat** | 10.1.20 | 内嵌 Web 服务器 |

#### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| **Vue 3** | 3.4.21 | 前端框架（Composition API） |
| **TypeScript** | 5.4.5 | 类型安全的 JavaScript 超集 |
| **Vite** | 5.2.10 | 前端构建工具（快速热更新） |
| **Vue Router** | 4.3.2 | 前端路由管理 |
| **Naive UI** | 2.38.2 | UI 组件库（按钮、表单、消息提示等） |
| **md-editor-v3** | 4.15.4 | Markdown 编辑器组件（用于编辑新闻） |
| **Axios** | 1.6.8 | HTTP 请求库（前后端通信） |

### 分层架构说明（后端）

```
HTTP 请求 → Controller（控制层）→ Service（业务层）→ Repository（数据层）→ MySQL 数据库
                 ↓                        ↓                      ↓
            接收请求参数            处理业务逻辑            执行 SQL 操作
            参数校验                密码加密/校验            CRUD 操作
            返回响应                权限判断                事务管理
```

各层职责：
- **Controller**：接收 HTTP 请求，调用 Service，返回 JSON 响应
- **Service**：核心业务逻辑（注册、登录、新闻增删改查）
- **Repository**：继承 JpaRepository，提供数据库 CRUD 操作
- **Entity/JPA Entity**：对应数据库表的 Java 类
- **DTO (Data Transfer Object)**：用于接收前端传来的请求数据

---

## 附录：快速启动清单（给老师看的版本）

> 如果你是为了演示/答辩，按以下步骤操作即可：

### 一次性准备（只需做一次）

1. ✅ 安装 JDK 17+
2. ✅ 安装 Node.js 18+
3. ✅ 安装 MySQL 8.0，记住 root 密码
4. ✅ 下载 Maven 并解压到任意位置
5. ✅ 导入数据库：`mysql -u root -p < backend/sql/newsdb.sql`
6. ✅ 修改 `application.yml` 中的数据库密码

### 每次启动（演示前）

```bash
# 终端 1：启动后端
cd backend
mvn spring-boot:run

# 终端 2：启动前端
cd frontend
npm run dev
```

然后浏览器打开 **http://localhost:5174**

---

*文档最后更新：2026年6月16日*
