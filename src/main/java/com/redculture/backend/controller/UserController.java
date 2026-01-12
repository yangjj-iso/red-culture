package com.redculture.backend.controller;

import com.redculture.backend.entity.SysUser;
import com.redculture.backend.mapper.SysUserMapper;
import com.redculture.backend.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;
    private final SysUserMapper sysUserMapper;

    /**
     * 获取所有用户列表（仅管理员）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SysUser> getAllUsers() {
        List<SysUser> users = sysUserService.list();
        // 为每个用户加载角色信息
        for (SysUser user : users) {
            user.setRoles(sysUserMapper.selectRolesByUserId(user.getId()));
        }
        return users;
    }

    /**
     * 修改用户角色（仅管理员）
     */
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean updateUserRole(@PathVariable Long userId, @RequestParam String role) {
        return sysUserService.updateUserRole(userId, role);
    }

    /**
     * 删除用户（仅管理员，危险操作）
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteUser(@PathVariable Long userId) {
        return sysUserService.removeById(userId);
    }
}
