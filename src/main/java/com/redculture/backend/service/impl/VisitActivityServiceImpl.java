package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.VisitActivity;
import com.redculture.backend.mapper.VisitActivityMapper;
import com.redculture.backend.service.VisitActivityService;
import org.springframework.stereotype.Service;

@Service
public class VisitActivityServiceImpl extends ServiceImpl<VisitActivityMapper, VisitActivity> implements VisitActivityService {
}

