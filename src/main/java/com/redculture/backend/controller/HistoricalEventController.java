package com.redculture.backend.controller;

import com.redculture.backend.entity.HistoricalEvent;
import com.redculture.backend.service.HistoricalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historical-events")
@RequiredArgsConstructor
public class HistoricalEventController {
    private final HistoricalEventService historicalEventService;

    @GetMapping
    public List<HistoricalEvent> list() {
        return historicalEventService.list();
    }

    @GetMapping("/{id}")
    public HistoricalEvent getById(@PathVariable Integer id) {
        return historicalEventService.getById(id);
    }

    @PostMapping
    public boolean create(@RequestBody HistoricalEvent historicalEvent) {
        return historicalEventService.save(historicalEvent);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Integer id, @RequestBody HistoricalEvent historicalEvent) {
        historicalEvent.setId(id);
        return historicalEventService.updateById(historicalEvent);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return historicalEventService.removeById(id);
    }
}

