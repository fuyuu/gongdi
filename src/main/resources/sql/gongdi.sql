/*
 Navicat Premium Dump SQL

 Source Server         : gongdi
 Source Server Type    : MySQL
 Source Server Version : 80410 (8.4.10)
 Source Host           : 49.235.164.243:3306
 Source Schema         : gongdi

 Target Server Type    : MySQL
 Target Server Version : 80410 (8.4.10)
 File Encoding         : 65001

 Date: 20/08/2026 22:50:26
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for approval_record
-- ----------------------------
DROP TABLE IF EXISTS `approval_record`;
CREATE TABLE `approval_record`
(
    `id`            bigint                                                       NOT NULL,
    `biz_type`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ATTENDANCE/EXPENSE/LOAN',
    `biz_id`        bigint                                                       NOT NULL COMMENT '业务ID',
    `approver_id`   bigint                                                       NOT NULL COMMENT '审批人',
    `action`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'APPROVED/REJECTED',
    `opinion`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
    `approval_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_biz`(`biz_type` ASC, `biz_id` ASC) USING BTREE,
    INDEX           `idx_approver`(`approver_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '审批记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment`
(
    `id`          bigint                                                        NOT NULL,
    `biz_type`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT 'ATTENDANCE/DAILY_REPORT/EXPENSE/LOAN',
    `biz_id`      bigint                                                        NOT NULL,
    `file_type`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'PHOTO/INVOICE/RECEIPT/OTHER',
    `file_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `file_url`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted`     tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_biz`(`biz_type` ASC, `biz_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`
(
    `id`                  bigint                                                       NOT NULL,
    `project_id`          bigint                                                       NOT NULL,
    `user_id`             bigint                                                       NOT NULL,
    `attendance_date`     date                                                         NOT NULL COMMENT '考勤日期',
    `check_in_time`       datetime NULL DEFAULT NULL COMMENT '签到时间',
    `check_in_longitude`  decimal(10, 7) NULL DEFAULT NULL,
    `check_in_latitude`   decimal(10, 7) NULL DEFAULT NULL,
    `check_in_address`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `check_out_time`      datetime NULL DEFAULT NULL COMMENT '签退时间',
    `check_out_longitude` decimal(10, 7) NULL DEFAULT NULL,
    `check_out_latitude`  decimal(10, 7) NULL DEFAULT NULL,
    `check_out_address`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `status`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL/LATE/EARLY/LEAVE/ABSENT/ABNORMAL',
    `remark`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `create_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`         datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`             tinyint                                                      NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_day`(`project_id` ASC, `user_id` ASC, `attendance_date` ASC) USING BTREE,
    INDEX                 `idx_project_date`(`project_id` ASC, `attendance_date` ASC) USING BTREE,
    INDEX                 `idx_user_date`(`user_id` ASC, `attendance_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attendance_apply
-- ----------------------------
DROP TABLE IF EXISTS `attendance_apply`;
CREATE TABLE `attendance_apply`
(
    `id`          bigint                                                        NOT NULL,
    `project_id`  bigint                                                        NOT NULL,
    `user_id`     bigint                                                        NOT NULL,
    `apply_type`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT 'LEAVE请假 / MAKEUP补卡 / OVERTIME加班',
    `start_time`  datetime NULL DEFAULT NULL,
    `end_time`    datetime NULL DEFAULT NULL,
    `reason`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原因',
    `status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/REJECTED',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_project_user`(`project_id` ASC, `user_id` ASC) USING BTREE,
    INDEX         `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for daily_report
-- ----------------------------
DROP TABLE IF EXISTS `daily_report`;
CREATE TABLE `daily_report`
(
    `id`                bigint                                                       NOT NULL,
    `project_id`        bigint                                                       NOT NULL,
    `user_id`           bigint                                                       NOT NULL COMMENT '填报人',
    `report_date`       date                                                         NOT NULL COMMENT '日报日期',
    `weather`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天气',
    `construction_area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '施工区域',
    `work_content`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '今日施工内容',
    `work_quantity`     decimal(12, 2) NULL DEFAULT NULL COMMENT '主要工程量',
    `work_unit`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
    `worker_count`      int NULL DEFAULT NULL COMMENT '现场人数',
    `problem`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '现场问题',
    `tomorrow_plan`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '明日计划',
    `status`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT/SUBMITTED/APPROVED/RETURNED',
    `create_time`       datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`           tinyint                                                      NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX               `idx_project_date`(`project_id` ASC, `report_date` ASC) USING BTREE,
    INDEX               `idx_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '施工日报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for expense
-- ----------------------------
DROP TABLE IF EXISTS `expense`;
CREATE TABLE `expense`
(
    `id`           bigint                                                        NOT NULL,
    `expense_no`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '报销单号',
    `project_id`   bigint                                                        NOT NULL,
    `user_id`      bigint                                                        NOT NULL COMMENT '报销人',
    `category`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT 'LABOR/MATERIAL/TRANSPORT/MEAL/HOTEL/OTHER',
    `expense_date` date                                                          NOT NULL COMMENT '发生日期',
    `amount`       decimal(12, 2)                                                NOT NULL COMMENT '金额',
    `payee_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收款方',
    `reason`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报销说明',
    `loan_id`      bigint NULL DEFAULT NULL COMMENT '关联借款ID',
    `status`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT/PENDING/APPROVED/REJECTED/PAID/VOID',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`      tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_expense_no`(`expense_no` ASC) USING BTREE,
    INDEX          `idx_project_date`(`project_id` ASC, `expense_date` ASC) USING BTREE,
    INDEX          `idx_user`(`user_id` ASC) USING BTREE,
    INDEX          `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '费用报销' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for loan
-- ----------------------------
DROP TABLE IF EXISTS `loan`;
CREATE TABLE `loan`
(
    `id`              bigint                                                        NOT NULL,
    `loan_no`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `project_id`      bigint                                                        NOT NULL,
    `user_id`         bigint                                                        NOT NULL,
    `amount`          decimal(12, 2)                                                NOT NULL COMMENT '借款金额',
    `purpose`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借款用途',
    `expected_date`   date NULL DEFAULT NULL COMMENT '预计核销日期',
    `returned_amount` decimal(12, 2)                                                NOT NULL DEFAULT 0.00 COMMENT '已退回金额',
    `status`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/PAID/SETTLED/REJECTED',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_loan_no`(`loan_no` ASC) USING BTREE,
    INDEX             `idx_project`(`project_id` ASC) USING BTREE,
    INDEX             `idx_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '借款记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`
(
    `id`           bigint                                                        NOT NULL,
    `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
    `project_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目编号',
    `address`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目地址',
    `manager_id`   bigint NULL DEFAULT NULL COMMENT '项目负责人',
    `start_date`   date NULL DEFAULT NULL COMMENT '开工日期',
    `end_date`     date NULL DEFAULT NULL COMMENT '计划完工日期',
    `longitude`    decimal(10, 7) NULL DEFAULT NULL COMMENT '项目经度',
    `latitude`     decimal(10, 7) NULL DEFAULT NULL COMMENT '项目纬度',
    `sign_radius`  int                                                           NOT NULL DEFAULT 300 COMMENT '签到范围/米',
    `status`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE/PAUSED/FINISHED',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`      tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_project_code`(`project_code` ASC) USING BTREE,
    INDEX          `idx_manager`(`manager_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_income
-- ----------------------------
DROP TABLE IF EXISTS `project_income`;
CREATE TABLE `project_income`
(
    `id`          bigint         NOT NULL,
    `project_id`  bigint         NOT NULL,
    `income_date` date           NOT NULL COMMENT '收款日期',
    `amount`      decimal(12, 2) NOT NULL COMMENT '收入金额',
    `payer_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '付款方',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint        NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_project_date`(`project_id` ASC, `income_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目收入' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_member
-- ----------------------------
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE `project_member`
(
    `id`          bigint                                                       NOT NULL,
    `project_id`  bigint                                                       NOT NULL,
    `user_id`     bigint                                                       NOT NULL,
    `team_id`     bigint NULL DEFAULT NULL,
    `role_code`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'WORKER' COMMENT 'BOSS/MANAGER/TEAM_LEADER/WORKER/FINANCE',
    `work_type`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工种',
    `join_date`   date NULL DEFAULT NULL COMMENT '进场日期',
    `leave_date`  date NULL DEFAULT NULL COMMENT '离场日期',
    `status`      tinyint                                                      NOT NULL DEFAULT 1,
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint                                                      NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_project_user`(`project_id` ASC, `user_id` ASC) USING BTREE,
    INDEX         `idx_user`(`user_id` ASC) USING BTREE,
    INDEX         `idx_team`(`team_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_team
-- ----------------------------
DROP TABLE IF EXISTS `project_team`;
CREATE TABLE `project_team`
(
    `id`          bigint                                                        NOT NULL,
    `project_id`  bigint                                                        NOT NULL COMMENT '项目ID',
    `team_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班组名称',
    `leader_id`   bigint NULL DEFAULT NULL COMMENT '班组长',
    `work_type`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工种',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint                                                       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_project`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目班组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint                                                       NOT NULL,
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
    `openid`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
    `avatar`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
    `status`      tinyint                                                      NOT NULL DEFAULT 1 COMMENT '1正常 0停用',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint                                                      NOT NULL DEFAULT 0,
    `role`        int NULL DEFAULT NULL COMMENT '1-用户 ,2-项目管理员, 3-系统管理员,4-该用户禁止使用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_phone
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_phone`;
CREATE TABLE `sys_user_phone`
(
    `id`      bigint NOT NULL,
    `phone`   bigint NULL DEFAULT NULL COMMENT '手机号',
    `user_id` bigint NULL DEFAULT NULL COMMENT '微信唯一id',
    `status`  int NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
