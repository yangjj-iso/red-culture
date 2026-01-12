package com.redculture.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redculture.backend.entity.EducationResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface EducationResourceMapper extends BaseMapper<EducationResource> {

    // 4. 查找某人物（如“李先念”）相关的所有教育资源（书籍、视频等）
    @Select("SELECT r.title, r.resource_type, r.description " +
            "FROM education_resource r " +
            "LEFT JOIN red_spot s ON r.red_spot_id = s.id " +
            "LEFT JOIN spot_figure_relation sfr ON s.id = sfr.red_spot_id " +
            "LEFT JOIN historical_figure f ON sfr.historical_figure_id = f.id " +
            "WHERE f.name = #{figureName} OR r.title LIKE CONCAT('%', #{figureName}, '%')")
    List<Map<String, Object>> findResourcesByFigureName(String figureName);
}
