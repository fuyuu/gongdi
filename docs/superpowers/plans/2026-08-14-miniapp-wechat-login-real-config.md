# Miniapp Wechat Login Real Config Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 打通工地小程序前端 `uni.login` 与后端真实微信 `jscode2session` 登录配置闭环。

**Architecture:** 后端保留现有 `/api/auth/wechat-login`、`IWechatService`、JWT 与拦截器结构，只把微信配置从 mock 默认切到真实配置，并允许本地私有配置覆盖。前端保留登录页和 store 流程，只把请求基地址抽成编译期/运行期可配置入口，避免真机联调时写死 `localhost`。

**Tech Stack:** Java 17, Spring Boot, JUnit MockMvc, uni-app Vue 3, TypeScript.

---

### Task 1: Backend Wechat Config

**Files:**
- Modify: `D:\develop\code\frontend\gongdi\gongdi\src\main\resources\application.properties`
- Create: `D:\develop\code\frontend\gongdi\gongdi\src\main\resources\application-local.properties`
- Modify: `D:\develop\code\frontend\gongdi\gongdi\src\test\java\com\gongdi\GongdiApiIntegrationTest.java`

- [ ] **Step 1: Write the failing config assertion**

Add a test assertion that the integration test context explicitly runs with `wechat.mock-enabled=true`, so future real-mode defaults do not break local tests.

- [ ] **Step 2: Run backend test to verify current config assumption**

Run: `mvn test`
Expected: tests should expose whether default mock settings are required by the login test.

- [ ] **Step 3: Implement real-mode configuration**

Set `spring.config.import=optional:application-local.properties`, configure `wechat.appid`, `wechat.secret`, and `wechat.mock-enabled` through environment placeholders, and place the provided local credentials in `application-local.properties`.

- [ ] **Step 4: Run backend tests**

Run: `mvn test`
Expected: all backend tests pass, with test class overriding mock mode.

### Task 2: Frontend Request Base URL

**Files:**
- Modify: `D:\develop\code\frontend\工地小程序\src\api\request.ts`
- Modify: `D:\develop\code\frontend\工地小程序\.env.example`

- [ ] **Step 1: Write the desired request base URL behavior**

Define a small `resolveBaseUrl()` helper so `VITE_API_BASE_URL` can override the default backend address.

- [ ] **Step 2: Run TypeScript check before implementation**

Run: `npm run type-check`
Expected: current code should compile or reveal unrelated type issues.

- [ ] **Step 3: Implement configurable base URL**

Use `import.meta.env.VITE_API_BASE_URL`, trim trailing slashes, and keep `http://localhost:8080` as fallback for H5/local developer runs.

- [ ] **Step 4: Run frontend checks**

Run: `npm run type-check` and `npm run build:mp-weixin`
Expected: TypeScript and WeChat mini-program build pass, or any unrelated existing issue is reported precisely.
