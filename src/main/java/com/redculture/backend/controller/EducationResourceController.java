package com.redculture.backend.controller;

import com.redculture.backend.entity.EducationResource;
import com.redculture.backend.service.EducationResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education-resources")
@RequiredArgsConstructor
public class EducationResourceController {
    private final EducationResourceService educationResourceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public List<EducationResource> list() {
        return educationResourceService.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'VISITOR', 'ADMIN', 'EDITOR')")
    public EducationResource getById(@PathVariable Integer id) {
        return educationResourceService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean create(@RequestBody EducationResource educationResource) {
        return educationResourceService.save(educationResource);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean update(@PathVariable Integer id, @RequestBody EducationResource educationResource) {
        educationResource.setId(id);
        return educationResourceService.updateById(educationResource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public boolean delete(@PathVariable Integer id) {
        return educationResourceService.removeById(id);
    }
}

