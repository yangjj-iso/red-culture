package com.redculture.backend.controller;

import com.redculture.backend.entity.Organization;
import com.redculture.backend.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public List<Organization> list() {
        return organizationService.list();
    }

    @GetMapping("/{id}")
    public Organization getById(@PathVariable Integer id) {
        return organizationService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping
    public boolean create(@RequestBody Organization organization) {
        return organizationService.save(organization);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping("/{id}")
    public boolean update(@PathVariable Integer id, @RequestBody Organization organization) {
        organization.setId(id);
        return organizationService.updateById(organization);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return organizationService.removeById(id);
    }
}

