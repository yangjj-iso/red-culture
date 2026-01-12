package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("visitor_feedback")
public class VisitorFeedback {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String visitorName;
    private String content;
    private Integer rating;
    private LocalDateTime feedbackTime;
    private Integer visitActivityId;
}
