package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.VisitorFeedback;
import com.redculture.backend.mapper.VisitorFeedbackMapper;
import com.redculture.backend.service.VisitorFeedbackService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class VisitorFeedbackServiceImpl extends ServiceImpl<VisitorFeedbackMapper, VisitorFeedback> implements VisitorFeedbackService {
    @Override
    public List<Map<String, Object>> findFeedbackByRating(Integer rating) {
        return baseMapper.findFeedbackByRating(rating);
    }
}
