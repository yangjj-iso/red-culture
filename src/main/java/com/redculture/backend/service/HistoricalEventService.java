package com.redculture.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redculture.backend.entity.HistoricalEvent;
import java.util.List;
import java.util.Map;

public interface HistoricalEventService extends IService<HistoricalEvent> {
    List<Map<String, Object>> findEventsAndFiguresBySpotName(String spotName);
}
