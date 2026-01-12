package com.redculture.backend.controller;

import com.redculture.backend.service.RedSpotService;
import com.redculture.backend.service.HistoricalEventService;
import com.redculture.backend.service.VisitorFeedbackService;
import com.redculture.backend.service.EducationResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final RedSpotService redSpotService;
    private final HistoricalEventService historicalEventService;
    private final VisitorFeedbackService visitorFeedbackService;
    private final EducationResourceService educationResourceService;

    // 1. 查询某景点（如“黄麻起义和鄂豫皖苏区纪念园”）的所有历史事件及其关联人物
    @GetMapping("/spot-events")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<Map<String, Object>> getEventsBySpot(@RequestParam String spotName) {
        return historicalEventService.findEventsAndFiguresBySpotName(spotName);
    }

    // 2. 统计指定月份（如2023-07）每个红色景点的参观团队数量
    @GetMapping("/visits-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public List<Map<String, Object>> getVisitsCount(@RequestParam String startDate, @RequestParam String endDate) {
        return redSpotService.countVisitsByDateRange(startDate, endDate);
    }

    // 3. 查询评分在指定分数（如5分）的反馈详情
    @GetMapping("/feedback-by-rating")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public List<Map<String, Object>> getFeedbackByRating(@RequestParam Integer rating) {
        return visitorFeedbackService.findFeedbackByRating(rating);
    }

    // 4. 查找某人物（如“李先念”）相关的所有教育资源
    @GetMapping("/resources-by-figure")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<Map<String, Object>> getResourcesByFigure(@RequestParam String figureName) {
        return educationResourceService.findResourcesByFigureName(figureName);
    }

    // 5. 查询所有“国家级”红色景点中，举办过“学校”类型单位参观活动的景点名称和活动次数
    @GetMapping("/school-visits-national")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public List<Map<String, Object>> getSchoolVisitsInNationalSpots() {
        return redSpotService.countSchoolVisitsInNationalSpots();
    }
}
