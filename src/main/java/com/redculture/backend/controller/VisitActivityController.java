package com.redculture.backend.controller;

import com.redculture.backend.entity.VisitActivity;
import com.redculture.backend.service.SysUserService;
import com.redculture.backend.service.VisitActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visit-activities")
@RequiredArgsConstructor
public class VisitActivityController {
    private final VisitActivityService visitActivityService;
    private final SysUserService sysUserService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @GetMapping
    public List<VisitActivity> list() {
        return visitActivityService.list();
    }

    @GetMapping("/{id}")
    public VisitActivity getById(@PathVariable Integer id) {
        return visitActivityService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping
    public boolean create(@RequestBody VisitActivity visitActivity) {
        return visitActivityService.save(visitActivity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping("/{id}")
    public boolean update(@PathVariable Integer id, @RequestBody VisitActivity visitActivity) {
        visitActivity.setId(id);
        return visitActivityService.updateById(visitActivity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return visitActivityService.removeById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    @PostMapping("/reserve")
    public boolean reserve(@RequestBody VisitActivity visitActivity) {
        // Set default status to Pending
        visitActivity.setStatus("Pending");
        // Ensure userId is set (could be taken from SecurityContext, but frontend passing it is fine for now)
        return visitActivityService.save(visitActivity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping("/{id}/audit")
    public boolean audit(@PathVariable Integer id, @RequestParam String status) {
        VisitActivity visitActivity = visitActivityService.getById(id);
        if (visitActivity != null) {
            visitActivity.setStatus(status);
            return visitActivityService.updateById(visitActivity);
        }
        return false;
    }

    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    @GetMapping("/my")
    public List<VisitActivity> myReservations() {
        String username = com.redculture.backend.security.SecurityUtils.getCurrentUsername();
        Integer userId = Math.toIntExact(sysUserService.findByUsername(username).getId());
        return visitActivityService.lambdaQuery().eq(VisitActivity::getUserId, userId).list();
    }
}

