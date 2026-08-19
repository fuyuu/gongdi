package com.gongdi;

import com.gongdi.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 后端接口集成测试，覆盖登录与各模块核心接口。
 */
@SpringBootTest(properties = "wechat.mock-enabled=true")
@AutoConfigureMockMvc
class GongdiApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockDataStore mockDataStore;

    /** 当前测试会话使用的访问令牌（模拟已登录的「张工」，userId=1） */
    private String accessToken;

    @BeforeEach
    void resetMockData() {
        mockDataStore.reset();
        accessToken = JwtTokenUtils.generateAccessToken(Map.of("userId", 1L, "role", "管理员"));
    }

    @Test
    void wxPhoneLoginReturnsTokenAndCurrentUserCanBeRead() throws Exception {
        mockMvc.perform(post("/api/auth/wx-phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loginCode\":\"zhanggong\",\"phoneCode\":\"13800000001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.accessToken", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.user.name", is("张工")));

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("张工")))
                .andExpect(jsonPath("$.data.currentProject", is("城南安置房二期")));
    }

    @Test
    void unknownPhoneAutoRegistersNewWxPhoneUser() throws Exception {
        mockMvc.perform(post("/api/auth/wx-phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loginCode\":\"newuser\",\"phoneCode\":\"13900000009\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.user.name", is("微信用户")))
                .andExpect(jsonPath("$.data.user.role", is("员工")))
                .andExpect(jsonPath("$.data.accessToken", not(emptyOrNullString())));
    }

    @Test
    void wxPhoneLoginRequiresSeparatedLoginCodeAndPhoneCode() throws Exception {
        mockMvc.perform(post("/api/auth/wx-phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneCode\":\"13800000001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.msg", is("微信登录 code 不能为空")));
    }

    @Test
    void projectAndHomeEndpointsReturnMiniProgramData() throws Exception {
        mockMvc.perform(get("/api/projects/my")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("城南安置房二期")));

        mockMvc.perform(get("/api/home")
                        .param("projectId", "1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.project.name", is("城南安置房二期")))
                .andExpect(jsonPath("$.data.todayOverview.userName", is("张工")))
                .andExpect(jsonPath("$.data.taskSummary.todo", is(2)))
                .andExpect(jsonPath("$.data.unreadMessageCount", is(3)));
    }

    @Test
    void currentUserCanListTasksAndUpdateOwnTaskStatus() throws Exception {
        mockMvc.perform(get("/api/tasks/my")
                        .param("projectId", "1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].title", is("钢筋绑扎（1层）")));

        mockMvc.perform(patch("/api/tasks/1002/status")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DOING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("DOING")))
                .andExpect(jsonPath("$.data.statusText", is("进行中")));
    }

    @Test
    void invalidTaskTransitionReturnsBusinessError() throws Exception {
        mockMvc.perform(patch("/api/tasks/1002/status")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PAUSED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.msg", is("普通员工只能将任务更新为进行中或已完成")));
    }

    @Test
    void messagesCanBeListedCountedAndMarkedRead() throws Exception {
        mockMvc.perform(get("/api/messages/unread-count")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.unreadCount", is(3)));

        mockMvc.perform(put("/api/messages/2001/read")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.read", is(true)));

        mockMvc.perform(get("/api/messages")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    @Test
    void attendancePreventsDuplicateCheckInAndAllowsCheckOut() throws Exception {
        mockMvc.perform(post("/api/attendance/check-in")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":1,\"latitude\":30.28,\"longitude\":120.15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checkedIn", is(true)));

        mockMvc.perform(post("/api/attendance/check-in")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":1,\"latitude\":30.28,\"longitude\":120.15}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg", is("今天已经签到，不能重复签到")));

        mockMvc.perform(post("/api/attendance/check-out")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checkedOut", is(true)));
    }
}
