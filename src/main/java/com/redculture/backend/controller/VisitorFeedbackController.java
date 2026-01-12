package com.redculture.backend.controller;

import com.redculture.backend.entity.VisitorFeedback;
import com.redculture.backend.service.VisitorFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitor-feedback")
@RequiredArgsConstructor
public class VisitorFeedbackController {
    private final VisitorFeedbackService visitorFeedbackService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<VisitorFeedback> list() {
        return visitorFeedbackService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public VisitorFeedback getById(@PathVariable Integer id) {
        return visitorFeedbackService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EDITOR')")
    public boolean create(@RequestBody VisitorFeedback visitorFeedback) {
        return visitorFeedbackService.save(visitorFeedback);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean update(@PathVariable Integer id, @RequestBody VisitorFeedback visitorFeedback) {
        visitorFeedback.setId(id);
        return visitorFeedbackService.updateById(visitorFeedback);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean delete(@PathVariable Integer id) {
        return visitorFeedbackService.removeById(id);
    }
}

