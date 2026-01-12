package com.redculture.backend.controller;

import com.redculture.backend.entity.RedSpot;
import com.redculture.backend.service.RedSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/red-spots")
@RequiredArgsConstructor
public class RedSpotController {
    private final RedSpotService redSpotService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<RedSpot> list() {
        return redSpotService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public RedSpot getById(@PathVariable Integer id) {
        return redSpotService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean create(@RequestBody RedSpot redSpot) {
        return redSpotService.save(redSpot);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean update(@PathVariable Integer id, @RequestBody RedSpot redSpot) {
        redSpot.setId(id);
        return redSpotService.updateById(redSpot);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean delete(@PathVariable Integer id) {
        return redSpotService.removeById(id);
    }
}

