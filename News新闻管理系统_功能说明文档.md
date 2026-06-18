**News 新闻管理系统**

**一、项目概述**

本项目是一个基于前后端分离架构的新闻管理系统，采用 MySQL 数据库 + Spring Boot 后端 + Vue 前端技术栈。系统支持用户注册与登录、新闻浏览与搜索、以及后台新闻发布等核心功能。

项目由两个子项目组成：

• news-spring-boot-backend：Spring Boot 后端服务，端口 8092

• news-spring-boot-frontend：Vue 前端项目，端口 5174

**二、技术架构**

**2.1 整体架构**

系统采用经典的三层架构：

浏览器（Vue 前端，5174 端口）→ 后端接口（Spring Boot，8092 端口）→ 数据库（MySQL，3306 端口）

**2.2 后端技术栈**

**技术**

**版本/说明**

**用途**

JDK

17

Java 运行环境

Maven

3.8+

依赖管理与项目构建

Spring Boot

—

后端框架

Spring Web

—

提供 RESTful API

Spring Data JPA

—

数据库 ORM 操作

Spring Security

—

安全认证与权限控制

MySQL Driver

—

连接 MySQL 数据库

BCrypt

—

密码加密存储

**2.3 前端技术栈**

**技术**

**版本/说明**

**用途**

Vite

8

构建工具与开发服务器

Vue

3

前端框架（Composition API + <script setup>）

TypeScript

—

类型安全的 JavaScript

Vue Router

4（Hash 模式）

前端路由管理

Naive UI

—

UI 组件库

md-editor-v3

—

Markdown 编辑与预览

axios

—

HTTP 请求库

**2.4 开发环境要求**

**软件**

**版本建议**

**用途**

JDK

17

运行 Java / Spring Boot

Maven

3.8+

管理后端依赖、打包

IntelliJ IDEA

社区版即可

Java 开发 IDE

Node.js

18+（推荐 20+）

运行前端

npm

8+

前端包管理

FlyEnv

—

一键启动 MySQL

Navicat

可选

可视化操作数据库

VS Code / WebStorm

—

Vue 代码开发

**三、数据库设计**

数据库名：newsdb，字符集：utf8mb4，排序规则：utf8mb4_unicode_ci

**3.1 用户表（user）**

**字段名**

**数据类型**

**约束**

**说明**

user_id

BIGINT

NOT NULL, AUTO_INCREMENT, PRIMARY KEY

用户ID，自增主键

email

VARCHAR(50)

NOT NULL, UNIQUE

邮箱，不可重复

username

VARCHAR(20)

NOT NULL, UNIQUE

用户名，登录用，不可重复

password

VARCHAR(100)

NULL

密码，BCrypt加密后的密文

**3.2 新闻表（news）**

**字段名**

**数据类型**

**约束**

**说明**

news_id

BIGINT(20)

NOT NULL, AUTO_INCREMENT, PRIMARY KEY

新闻ID，自增主键

title

VARCHAR(255)

NULL, DEFAULT NULL

新闻标题

content

LONGTEXT

NULL

新闻正文，支持 Markdown 格式

creation

DATETIME(6)

NULL, DEFAULT NULL

创建时间

**四、功能模块说明**

**4.1 用户模块**

用户模块负责用户的注册、登录、身份验证及状态管理。

**功能**

**说明**

**对应接口**

**是否需要登录**

用户注册

填写邮箱、用户名、密码完成注册；密码使用 BCrypt 加密后存入数据库

POST /users/register

否

用户登录

使用用户名和密码登录；成功后返回用户凭证

POST /users/login

否

登录态保持

凭证保存在 localStorage，刷新页面仍保持登录状态

—

是

退出登录

清除 localStorage 中的登录态，跳转至登录页

—

是

获取当前用户

查看当前登录用户的基本信息

GET /users/me

是（HTTP Basic）

**4.2 新闻模块**

新闻模块负责新闻的展示、搜索和详情查看。

**功能**

**说明**

**对应接口**

**是否需要登录**

新闻列表

展示全部新闻标题，点击跳转详情页

GET /news

否

关键字搜索

按标题或正文进行模糊搜索，支持搜索、重置、回车触发

GET /news?keyword=关键字

否

新闻详情

根据 ID 显示单条新闻，使用 Markdown 渲染预览，显示标题与发布时间

GET /news/{id}

否

返回列表

详情页提供「返回」按钮，浏览器历史回退

—

是

**4.3 后台发布模块**

后台模块供已登录用户发布新闻，采用 HTTP Basic 认证。

**功能**

**说明**

**对应接口**

**是否需要登录**

后台连接检测

进入页面时请求 /api/admins/hi，显示 Hello World! 或认证失败提示

GET /admins/hi

是（HTTP Basic）

Markdown 编辑

双栏编辑器，中文工具栏（zh-CN），支持实时预览

—

是

发布新闻

填写标题与正文，点击「发布新闻」或编辑器「保存」图标提交

POST /admins/news

是（HTTP Basic）

表单校验

标题、正文不能为空；发布过程中防重复点击

—

是

**五、页面与路由**

路由模式为 Hash 模式（URL 中带 #），部署简单。

**路由路径**

**对应页面**

**是否需要登录**

**说明**

#/register

注册页

否（已登录会跳转首页）

新用户注册

#/login

登录页

否（已登录会跳转首页）

用户登录

#/

新闻列表

是

展示新闻，支持搜索

#/news/:id

新闻详情

是

查看单条新闻详情

#/admin

后台发布

是

发布 Markdown 新闻

顶部导航（登录后可见）：新闻列表、后台发布、欢迎用户名、退出。

路由守卫：未登录用户无法访问新闻列表、详情与后台发布；已登录用户访问登录/注册页会自动跳转首页。

**六、后端接口说明**

所有接口均通过前端代理访问，前端请求 /api/xxx 会自动转发到后端 http://localhost:8092/xxx。

**方法**

**路径**

**说明**

**认证方式**

GET

/news

获取新闻列表

无

GET

/news?keyword=关键字

按标题/正文模糊搜索

无

GET

/news/{id}

获取新闻详情

无

POST

/users/register

用户注册

无

POST

/users/login

用户登录

无

GET

/users/me

获取当前用户信息

HTTP Basic

GET

/admins/hi

后台连通测试

HTTP Basic

POST

/admins/news

发布新闻

HTTP Basic

**七、项目结构**

**7.1 后端项目结构**

news-spring-boot-backend/

├── pom.xml                          ← Maven 依赖配置

├── sql/

│   └── newsdb.sql                   ← 建库建表脚本

└── src/main/

    ├── java/com/news/

    │   ├── NewsApplication.java     ← 启动入口（main 方法）

    │   ├── config/                  ← 安全配置、初始数据

    │   ├── controller/              ← 接口层（对外提供 URL）

    │   ├── dto/                     ← 接收前端 JSON 的参数类

    │   ├── entity/                  ← 实体类（对应数据库表）

    │   ├── repository/              ← 数据库操作层

    │   └── service/                 ← 业务逻辑层

    └── resources/

        └── application.yml          ← 数据库连接、端口等配置

**7.2 前端项目结构**

news-spring-boot-frontend/

├── index.html

├── package.json

├── vite.config.ts          ← Vite 配置、开发端口、API 代理

├── tsconfig.json

└── src/

    ├── main.ts             ← 应用入口

    ├── App.vue             ← 根组件、顶部导航

    ├── auth.ts             ← 登录态读写（localStorage）

    ├── news.ts             ← News 类型定义

    ├── style.css           ← 全局样式

    ├── router/

    │   └── index.ts        ← 路由与守卫

    └── components/

        ├── Login.vue       ← 登录页

        ├── Register.vue    ← 注册页

        ├── NewsList.vue    ← 新闻列表 + 搜索

        ├── NewsDetail.vue  ← 新闻详情

        └── Admin.vue       ← 后台 Markdown 发布

**8.1 数据库搭建（考核基础）**

**考核要求：**

1. 正确启动 MySQL 服务，端口 3306 正常运行。

2. 使用 Navicat 成功连接数据库，主机 localhost，用户名/密码正确。

3. 执行 newsdb.sql 脚本，成功创建 newsdb 数据库。

4. 数据库中必须包含 user 和 news 两张表，表结构符合设计要求。

5. 验证方式：在 Navicat 左侧展开连接，应能看到 newsdb → 表 → user、news。

**8.2 后端功能实现（考核核心）**

**考核要求：后端项目必须完整实现以下接口功能，且能正常响应。**

**8.2.1 用户注册功能**

• 接口：POST /users/register

• 功能：接收邮箱、用户名、密码，使用 BCrypt 加密密码后存入 user 表。

• 验证：注册成功后，Navicat 中 user 表应新增一条记录，password 字段为密文（非明文）。

• 约束：email 和 username 必须唯一，重复注册应返回错误提示。

**8.2.2 用户登录功能**

• 接口：POST /users/login

• 功能：根据用户名查询数据库，使用 BCrypt 校验密码，成功后返回用户凭证。

• 验证：使用刚注册的账号能正常登录，错误密码应返回认证失败。

**8.2.3 新闻列表查询**

• 接口：GET /news

• 功能：返回全部新闻列表（JSON 格式），支持空列表返回 []。

• 验证：浏览器访问 http://localhost:8092/news 应返回 JSON 数据。

**8.2.4 新闻搜索功能**

• 接口：GET /news?keyword=关键字

• 功能：按标题或正文进行模糊匹配搜索，返回符合条件的新闻列表。

• 验证：传入不同关键字，返回结果应准确过滤。

**8.2.5 新闻详情查询**

• 接口：GET /news/{id}

• 功能：根据新闻 ID 返回单条新闻的完整信息（标题、内容、创建时间）。

• 验证：传入有效 ID 返回对应新闻，无效 ID 返回合理提示。

**8.2.6 后台权限测试**

• 接口：GET /admins/hi

• 功能：使用 HTTP Basic 认证，验证当前用户是否有后台访问权限。

• 验证：携带正确用户名密码返回 Hello World!，错误凭证返回 401 认证失败。

**8.2.7 新闻发布功能**

• 接口：POST /admins/news

• 功能：接收标题和 Markdown 内容，存入 news 表，自动生成创建时间。

• 验证：发布后 Navicat 中 news 表新增记录，首页列表能显示新发布的新闻。

• 约束：标题和内容不能为空，发布过程中需防重复提交。

**8.3 前端功能实现（考核核心）**

**考核要求：前端项目必须完整实现以下页面功能，且与后端接口正常联调。**

**8.3.1 注册页面（Register.vue）**

• 页面路径：#/register

• 功能：表单包含邮箱、用户名、密码输入框，点击注册按钮调用 POST /api/users/register。

• 验证：填写有效信息提交后，应提示注册成功并跳转登录页。

• 约束：已登录用户访问注册页应自动跳转首页。

**8.3.2 登录页面（Login.vue）**

• 页面路径：#/login

• 功能：表单包含用户名、密码输入框，点击登录按钮调用 POST /api/users/login。

• 验证：登录成功后跳转新闻列表，localStorage 中保存登录凭证。

• 约束：已登录用户访问登录页应自动跳转首页；错误密码应提示失败。

**8.3.3 新闻列表页面（NewsList.vue）**

• 页面路径：#/

• 功能：

  - 展示全部新闻标题列表，点击标题跳转详情页。

  - 提供搜索框，支持按关键字模糊搜索（调用 GET /api/news?keyword=xxx）。

  - 支持搜索按钮、重置按钮、回车键触发搜索。

• 验证：页面加载时显示新闻列表，搜索后显示过滤结果，重置后恢复全部列表。

• 约束：未登录用户访问此页应被路由守卫拦截，跳转登录页。

**8.3.4 新闻详情页面（NewsDetail.vue）**

• 页面路径：#/news/:id

• 功能：

  - 根据 URL 中的 ID 参数调用 GET /api/news/{id} 获取新闻详情。

  - 使用 Markdown 渲染新闻正文内容。

  - 显示新闻标题和发布时间。

  - 提供「返回」按钮，点击后浏览器历史回退。

• 验证：能正确显示新闻内容，Markdown 格式正常渲染。

• 约束：未登录用户访问此页应被路由守卫拦截。

**8.3.5 后台发布页面（Admin.vue）**

• 页面路径：#/admin

• 功能：

  - 进入页面时自动请求 GET /api/admins/hi，检测后台连接状态。

  - 使用 md-editor-v3 双栏 Markdown 编辑器（中文工具栏 zh-CN）。

  - 填写标题和正文，点击「发布新闻」或编辑器「保存」图标提交。

  - 调用 POST /api/admins/news，使用 HTTP Basic 认证（当前登录用户的用户名和密码）。

  - 表单校验：标题和内容不能为空。

  - 发布过程中防重复点击。

• 验证：发布成功后新闻列表页能显示新发布的新闻。

• 约束：未登录用户访问此页应被路由守卫拦截。

**8.3.6 顶部导航与退出功能**

• 功能：登录后在页面顶部显示导航栏，包含：新闻列表链接、后台发布链接、欢迎语（显示当前用户名）、退出按钮。

• 验证：点击「退出」后清除 localStorage 登录态，跳转登录页，导航栏消失。

**8.4 路由守卫功能（考核核心）**

**考核要求：路由守卫必须正确实现，保障页面访问权限。**

• 未登录状态：访问 /（新闻列表）、/news/:id（详情）、/admin（后台）时，自动跳转 #/login。

• 已登录状态：访问 #/register 或 #/login 时，自动跳转 #/（首页）。

• 验证：分别测试登录/未登录两种状态，确认跳转行为正确。