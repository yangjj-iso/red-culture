package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("historical_event")
public class HistoricalEvent {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private LocalDate eventDate;
    private String description;
    private Integer redSpotId;
}
