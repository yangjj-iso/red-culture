package com.redculture.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redculture.backend.entity.SysUser;
import com.redculture.backend.entity.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT id FROM sys_role WHERE name = #{roleName}")
    Long selectRoleIdByRoleName(@Param("roleName") String roleName);

    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    boolean deleteUserRoles(@Param("userId") Long userId);

    @Insert("INSERT INTO sys_user_role (user_id, role_id) " +
            "SELECT #{userId}, id FROM sys_role WHERE name = #{roleName}")
    boolean insertUserRole(@Param("userId") Long userId, @Param("roleName") String roleName);

    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertUserRoleByRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
