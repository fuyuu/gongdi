# 工地小程序后端内存版 MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现一个可运行的 SpringBoot 内存版后端 MVP，让工地小程序可以先替换核心 mock 数据。

**Architecture:** 先用内存仓库承载用户、项目、任务、消息、考勤数据，控制器只处理 REST 入参出参，业务规则集中在 service 层。接口统一返回 `ApiResponse`，当前登录用户由 `Authorization: Bearer <token>` 解析，后续替换 MyBatis-Plus/MySQL 时保留 controller/service/DTO 契约。

**Tech Stack:** Java 17, Spring Boot 4.1.0, Spring WebMVC, JUnit 5, MockMvc.

---

### Task 1: 接口契约测试

**Files:**
- Create: `src/test/java/com/gongdi/GongdiApiIntegrationTest.java`

- [ ] **Step 1: Write failing tests**

覆盖微信登录、当前用户、我的项目、首页聚合、我的任务、任务状态更新、消息读取、签到签退。

- [ ] **Step 2: Run tests and verify failure**

Run: `.\mvnw.cmd test`
Expected: FAIL，原因是接口尚未实现或返回 404。

### Task 2: 基础设施

**Files:**
- Create: `src/main/java/com/gongdi/common/ApiResponse.java`
- Create: `src/main/java/com/gongdi/common/BizException.java`
- Create: `src/main/java/com/gongdi/common/GlobalExceptionHandler.java`
- Create: `src/main/java/com/gongdi/auth/LoginUser.java`
- Create: `src/main/java/com/gongdi/auth/AuthService.java`

- [ ] **Step 1: Implement common response and exception handling**

所有接口成功返回 `code=0`，业务异常返回 `code` 和中文 `message`。

- [ ] **Step 2: Implement token parsing**

内存版 token 使用 `mock-token-{userId}`，便于小程序联调和测试。

### Task 3: 内存业务模型与服务

**Files:**
- Create: `src/main/java/com/gongdi/mock/MockDataStore.java`
- Create: `src/main/java/com/gongdi/home/HomeService.java`
- Create: `src/main/java/com/gongdi/task/TaskService.java`
- Create: `src/main/java/com/gongdi/message/MessageService.java`
- Create: `src/main/java/com/gongdi/attendance/AttendanceService.java`

- [ ] **Step 1: Implement seeded data**

初始化张工、城南安置房二期、任务、消息、工资摘要和今日考勤数据。

- [ ] **Step 2: Implement business validations**

普通员工只能操作自己的数据；任务只能从 `TODO` 更新为 `DOING` 或 `DONE`；同用户同项目同日只能签到一次。

### Task 4: REST Controllers

**Files:**
- Create: `src/main/java/com/gongdi/auth/AuthController.java`
- Create: `src/main/java/com/gongdi/user/UserController.java`
- Create: `src/main/java/com/gongdi/project/ProjectController.java`
- Create: `src/main/java/com/gongdi/home/HomeController.java`
- Create: `src/main/java/com/gongdi/task/TaskController.java`
- Create: `src/main/java/com/gongdi/message/MessageController.java`
- Create: `src/main/java/com/gongdi/attendance/AttendanceController.java`

- [ ] **Step 1: Expose MVP endpoints**

所有新增功能点添加中文注释，接口路径保持 `/api/...`。

- [ ] **Step 2: Run tests and verify pass**

Run: `.\mvnw.cmd test`
Expected: PASS。
