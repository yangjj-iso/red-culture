package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.EducationResource;
import com.redculture.backend.mapper.EducationResourceMapper;
import com.redculture.backend.service.EducationResourceService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class EducationResourceServiceImpl extends ServiceImpl<EducationResourceMapper, EducationResource> implements EducationResourceService {
    @Override
    public List<Map<String, Object>> findResourcesByFigureName(String figureName) {
        return baseMapper.findResourcesByFigureName(figureName);
    }
}
