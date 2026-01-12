package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("red_spot")
public class RedSpot {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String protectionLevel;
    private String historyBackground;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createdAt;
}
