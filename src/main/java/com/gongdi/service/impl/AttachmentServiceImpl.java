package com.gongdi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongdi.domain.entity.Attachment;
import com.gongdi.mapper.AttachmentMapper;
import com.gongdi.service.IAttachmentService;
import org.springframework.stereotype.Service;

/**
 * 附件 Service 实现。
 *
 * @author Ma Qiang
 * @since 2026/8/20
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {
}