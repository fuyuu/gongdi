package com.gongdi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongdi.domain.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件 Mapper，对应 attachment 表。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
}