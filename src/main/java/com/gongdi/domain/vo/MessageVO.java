package com.gongdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息响应对象，包含未读状态和图标颜色。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {

    /**
     * 消息 ID
     */
    private Long id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String description;

    /**
     * 时间文本
     */
    private String time;

    /**
     * 是否未读（保持原 record 的 JSON 字段名 isUnread）
     */
    @JsonProperty("isUnread")
    private boolean unread;

    /**
     * 图标颜色
     */
    private String iconColor;
}
