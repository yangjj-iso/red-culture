package com.redculture.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redculture.backend.entity.VisitorFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface VisitorFeedbackMapper extends BaseMapper<VisitorFeedback> {

    // 3. 查询评分在指定分数（如5分）的反馈详情，显示活动主题、单位名称和反馈内容
    @Select("SELECT va.theme, o.name AS unitName, vf.visitor_name, vf.content " +
            "FROM visitor_feedback vf " +
            "JOIN visit_activity va ON vf.visit_activity_id = va.id " +
            "JOIN organization o ON va.organization_id = o.id " +
            "WHERE vf.rating = #{rating}")
    List<Map<String, Object>> findFeedbackByRating(Integer rating);
}
