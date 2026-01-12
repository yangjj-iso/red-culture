package com.redculture.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("spot_figure_relation")
public class SpotFigureRelation {
    private Integer redSpotId;
    private Integer historicalFigureId;
}
