package com.redculture.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redculture.backend.entity.HistoricalEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface HistoricalEventMapper extends BaseMapper<HistoricalEvent> {

    // 1. 查询某景点（如“黄麻起义和鄂豫皖苏区纪念园”）的所有历史事件及其关联人物
    @Select("SELECT e.title AS eventTitle, e.event_date AS eventDate, f.name AS figureName " +
            "FROM historical_event e " +
            "JOIN red_spot s ON e.red_spot_id = s.id " +
            "LEFT JOIN spot_figure_relation sfr ON s.id = sfr.red_spot_id " +
            "LEFT JOIN historical_figure f ON sfr.historical_figure_id = f.id " +
            "WHERE s.name = #{spotName}")
    List<Map<String, Object>> findEventsAndFiguresBySpotName(String spotName);
}
