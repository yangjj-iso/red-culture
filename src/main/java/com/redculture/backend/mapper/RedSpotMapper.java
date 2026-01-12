package com.redculture.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redculture.backend.entity.RedSpot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface RedSpotMapper extends BaseMapper<RedSpot> {
    
    // 5. 查询所有“国家级”红色景点中，举办过“学校”类型单位参观活动的景点名称和活动次数
    @Select("SELECT s.name AS spotName, COUNT(v.id) AS schoolVisitCount " +
            "FROM red_spot s " +
            "JOIN visit_activity v ON s.id = v.red_spot_id " +
            "JOIN organization o ON v.organization_id = o.id " +
            "WHERE s.protection_level = '国家级' AND o.org_type = 'School' " +
            "GROUP BY s.id, s.name")
    List<Map<String, Object>> countSchoolVisitsInNationalSpots();

    // 2. 统计指定月份（如2023-07）每个红色景点的参观团队数量，按数量降序排列
    @Select("SELECT s.name AS spotName, COUNT(v.id) AS visitCount " +
            "FROM red_spot s " +
            "JOIN visit_activity v ON s.id = v.red_spot_id " +
            "WHERE v.visit_time >= CONCAT(#{startDate}, ' 00:00:00') AND v.visit_time <= CONCAT(#{endDate}, ' 23:59:59') " +
            "GROUP BY s.id, s.name " +
            "ORDER BY visitCount DESC")
    List<Map<String, Object>> countVisitsByDateRange(String startDate, String endDate);
}
