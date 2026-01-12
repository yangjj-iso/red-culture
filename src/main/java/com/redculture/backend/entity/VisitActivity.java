package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("visit_activity")
public class VisitActivity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String theme;
    private LocalDateTime visitTime;
    private Integer participantCount;
    private String status; // ENUM ('Reserved', 'Completed', 'Cancelled')
    private Integer organizationId;
    private Integer redSpotId;
    private Integer userId;
}
