package com.redculture.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redculture.backend.entity.SysUser;
import com.redculture.backend.mapper.SysUserMapper;
import com.redculture.backend.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findByUsername(String username) {
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("username", username));
        if (user != null) {
            user.setRoles(sysUserMapper.selectRolesByUserId(user.getId()));
        }
        return user;
    }

    @Override
    public void registerUser(SysUser user) {
        save(user);
    }

    @Override
    @Transactional
    public boolean updateUserRole(Long userId, String role) {
        // 先删除用户的所有角色
        sysUserMapper.deleteUserRoles(userId);
        // 插入新角色
        return sysUserMapper.insertUserRole(userId, role);
    }

    @Override
    public boolean assignRoleToUser(Long userId, Long roleId) {
        return sysUserMapper.insertUserRoleByRoleId(userId, roleId) > 0;
    }
}
