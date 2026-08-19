package com.gongdi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内消息实体，unread 表示消息是否未读。
 * @author Ma Qiang
 * @since 2026/8/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 消息 ID
     */
    private Long id;

    /**
     * 接收用户 ID
     */
    private Long userId;

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
     * 是否未读
     */
    private boolean unread;
}
