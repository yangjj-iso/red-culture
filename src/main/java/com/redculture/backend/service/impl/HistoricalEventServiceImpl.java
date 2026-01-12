package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.HistoricalEvent;
import com.redculture.backend.mapper.HistoricalEventMapper;
import com.redculture.backend.service.HistoricalEventService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class HistoricalEventServiceImpl extends ServiceImpl<HistoricalEventMapper, HistoricalEvent> implements HistoricalEventService {
    @Override
    public List<Map<String, Object>> findEventsAndFiguresBySpotName(String spotName) {
        return baseMapper.findEventsAndFiguresBySpotName(spotName);
    }
}
