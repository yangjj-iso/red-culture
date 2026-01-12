package com.redculture.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redculture.backend.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser findByUsername(String username);
    void registerUser(SysUser user);
    boolean updateUserRole(Long userId, String role);
    boolean assignRoleToUser(Long userId, Long roleId);
}
