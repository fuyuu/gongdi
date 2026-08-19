# Miniapp Wechat Login V1 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 打通工地小程序 `uni.login` 到后端 `/api/auth/wechat-login` 的微信登录闭环。

**Architecture:** 前端新增轻量请求封装，登录 store 负责调用微信登录、提交 code、保存后端 JWT 与用户信息。后端沿用现有微信登录接口、JWT 与拦截器，只在编译或契约不一致时做最小修补。

**Tech Stack:** uni-app Vue 3 TypeScript, Spring Boot 4.1, Java 17, JWT, 微信小程序登录 code2session。

---

### Task 1: 前端登录契约

**Files:**
- Create: `D:\develop\code\frontend\工地小程序\src\api\request.ts`
- Create: `D:\develop\code\frontend\工地小程序\src\api\auth.ts`
- Modify: `D:\develop\code\frontend\工地小程序\src\store\auth.ts`

- [ ] **Step 1: Create request wrapper**

实现 `request<T>()`，统一 baseUrl、`Authorization` 请求头、`Result<T>` 业务码判断。

- [ ] **Step 2: Create auth API**

实现 `wechatLogin(code)`，调用 `POST /api/auth/wechat-login`，返回 `accessToken/tokenType/user`。

- [ ] **Step 3: Replace mock login**

保留本地角色选择状态，把 `login(role)` 改为异步 `loginByWechat(role)`：先 `uni.login`，再调用后端，最后保存 token、role、user。

### Task 2: 登录页接入

**Files:**
- Modify: `D:\develop\code\frontend\工地小程序\src\pages\login\index.vue`

- [ ] **Step 1: Call async login**

登录按钮调用 `await loginByWechat(selectedRole.value)`，移除模拟延迟。

- [ ] **Step 2: Preserve navigation**

登录成功后继续按所选身份跳转到员工端或管理端首页。

### Task 3: 验证

**Files:**
- Verify: `D:\develop\code\frontend\工地小程序\package.json`
- Verify: `D:\develop\code\frontend\gongdi\gongdi\pom.xml`

- [ ] **Step 1: Frontend type/build check**

运行 `npm.cmd run type-check`，如项目现有类型债阻塞，再运行 `npm.cmd run build:mp-weixin` 获取实际小程序编译信号。

- [ ] **Step 2: Backend compile check**

运行 `.\mvnw.cmd -q test` 或 `.\mvnw.cmd -q -DskipTests compile`，确认后端登录接口仍可编译。

- [ ] **Step 3: Report exact status**

分别报告前端接入、后端状态、验证命令结果，不把环境/依赖失败说成代码通过。
