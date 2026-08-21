package com.gongdi.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 班组成员实体，对应 team_member 表。
 *
 * @author Ma Qiang
 * @since 2026/8/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("team_member")
public class TeamMember {

    /** 主键，数据库自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目 ID */
    private Long projectId;

    /** 班组 ID */
    private Long teamId;

    /** 用户 ID */
    private Long userId;

    /** 加入班组日期 */
    private LocalDate joinDate;

    /** 离开班组日期 */
    private LocalDate leaveDate;

    /** 状态：ACTIVE / LEFT */
    private String status;
}
