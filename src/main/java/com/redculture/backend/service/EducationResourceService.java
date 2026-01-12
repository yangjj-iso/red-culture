package com.redculture.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redculture.backend.entity.EducationResource;
import java.util.List;
import java.util.Map;

public interface EducationResourceService extends IService<EducationResource> {
    List<Map<String, Object>> findResourcesByFigureName(String figureName);
}
