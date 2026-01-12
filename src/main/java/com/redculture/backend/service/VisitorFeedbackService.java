package com.redculture.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redculture.backend.entity.VisitorFeedback;
import java.util.List;
import java.util.Map;

public interface VisitorFeedbackService extends IService<VisitorFeedback> {
    List<Map<String, Object>> findFeedbackByRating(Integer rating);
}
