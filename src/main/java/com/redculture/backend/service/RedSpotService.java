package com.redculture.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redculture.backend.entity.RedSpot;
import java.util.List;
import java.util.Map;

public interface RedSpotService extends IService<RedSpot> {
    List<Map<String, Object>> countSchoolVisitsInNationalSpots();
    List<Map<String, Object>> countVisitsByDateRange(String startDate, String endDate);
}
