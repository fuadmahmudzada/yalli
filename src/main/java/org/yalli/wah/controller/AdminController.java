package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.model.dto.AdminDto;
import org.yalli.wah.model.dto.AdminLightDto;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.NotificationSaveDto;
import org.yalli.wah.service.AdminService;
import org.yalli.wah.service.PermissionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PermissionService permissionService;

    @GetMapping
    public List<AdminLightDto> getAdmins(Pageable pageable,
                                         @RequestParam(required = false) String fullName,
                                         @RequestHeader("user-id") Long userId) {
        if (!permissionService.hasPermission(userId, "view")) {
            return List.of();
        }
        return adminService.getAllAdmins(pageable, fullName);
    }

    @GetMapping("/{adminId}")
    public AdminDto getAdmin(@PathVariable Long adminId, @RequestHeader("user-id") Long userId) {
        if (!permissionService.hasPermission(userId, "view")) {
            return null;
        }
        return adminService.getAdmin(adminId);
    }

    @PostMapping
    public void createAdmin(@RequestBody AdminDto adminDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "create")) {
            adminService.saveAdmin(adminDto, userId);
        }
    }

    @PutMapping("/{adminId}")
    public void updateAdmin(@PathVariable Long adminId,
                            @RequestBody AdminDto adminDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "updateAdmin")) {
            adminService.editAdmin(adminId, adminDto, userId);
        }
    }

    @PatchMapping("/{adminId}")
    public void resetPassword(@PathVariable Long adminId, @RequestBody Map<String, String> body,
                              @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "resetPassword")) {
            adminService.resetPassword(adminId, body.get("password"), userId);
        }
    }

    @DeleteMapping("/{adminId}")
    public void deleteAdmin(@PathVariable Long adminId, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "deleteAdmin")) {
            adminService.deleteAdmin(adminId, userId);
        }
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto) {
        return adminService.login(loginDto);
    }

    @PostMapping("/add-notification")
    public void addNotification(@RequestBody NotificationSaveDto notificationSaveDto,
                                @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "create")) {
            adminService.saveNotification(notificationSaveDto, userId);
        }
    }
}
