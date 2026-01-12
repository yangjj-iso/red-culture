package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.RedSpot;
import com.redculture.backend.mapper.RedSpotMapper;
import com.redculture.backend.service.RedSpotService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class RedSpotServiceImpl extends ServiceImpl<RedSpotMapper, RedSpot> implements RedSpotService {
    @Override
    public List<Map<String, Object>> countSchoolVisitsInNationalSpots() {
        return baseMapper.countSchoolVisitsInNationalSpots();
    }

    @Override
    public List<Map<String, Object>> countVisitsByDateRange(String startDate, String endDate) {
        return baseMapper.countVisitsByDateRange(startDate, endDate);
    }
}
