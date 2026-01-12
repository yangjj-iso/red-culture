package com.redculture.backend.controller;

import com.redculture.backend.entity.HistoricalFigure;
import com.redculture.backend.service.HistoricalFigureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historical-figures")
@RequiredArgsConstructor
public class HistoricalFigureController {
    private final HistoricalFigureService historicalFigureService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<HistoricalFigure> list() {
        return historicalFigureService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public HistoricalFigure getById(@PathVariable Integer id) {
        return historicalFigureService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean create(@RequestBody HistoricalFigure historicalFigure) {
        return historicalFigureService.save(historicalFigure);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean update(@PathVariable Integer id, @RequestBody HistoricalFigure historicalFigure) {
        historicalFigure.setId(id);
        return historicalFigureService.updateById(historicalFigure);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean delete(@PathVariable Integer id) {
        return historicalFigureService.removeById(id);
    }
}

