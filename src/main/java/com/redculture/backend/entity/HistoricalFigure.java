package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("historical_figure")
public class HistoricalFigure {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String biography;
    private String hometown;
}
