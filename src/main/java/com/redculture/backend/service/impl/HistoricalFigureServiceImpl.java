package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.HistoricalFigure;
import com.redculture.backend.mapper.HistoricalFigureMapper;
import com.redculture.backend.service.HistoricalFigureService;
import org.springframework.stereotype.Service;

@Service
public class HistoricalFigureServiceImpl extends ServiceImpl<HistoricalFigureMapper, HistoricalFigure> implements HistoricalFigureService {
}

