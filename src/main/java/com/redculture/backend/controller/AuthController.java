package com.redculture.backend.controller;

import com.redculture.backend.dto.JwtResponse;
import com.redculture.backend.dto.LoginRequest;
import com.redculture.backend.dto.RegisterRequest;
import com.redculture.backend.entity.SysRole;
import com.redculture.backend.entity.SysUser;
import com.redculture.backend.security.JwtUtils;
import com.redculture.backend.service.SysRoleService;
import com.redculture.backend.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();    
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        SysUser user = sysUserService.findByUsername(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt, 
                                                 user.getId(), 
                                                 user.getUsername(), 
                                                 user.getEmail(), 
                                                 roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", signUpRequest.getUsername())) != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (sysUserService.getOne(new QueryWrapper<SysUser>().eq("email", signUpRequest.getEmail())) != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        SysUser user = new SysUser();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setFullName(signUpRequest.getFullName());
        user.setStatus(1); // Default active

        sysUserService.registerUser(user);
        
        // Handle Roles (Simplified: default to ROLE_USER if not specified, or handle request roles)
        // For now, we just create the user. Assigning roles would require inserting into sys_user_role.
        // Let's assign ROLE_USER by default.

        SysRole userRole = sysRoleService.getOne(new QueryWrapper<SysRole>().eq("name", "ROLE_USER"));
        if (userRole != null) {
            sysUserService.assignRoleToUser(user.getId(), userRole.getId());
        }

        return ResponseEntity.ok("User registered successfully!");
    }
}
