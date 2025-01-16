package org.yalli.wah.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.yalli.wah.dao.repository.EventRepository;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.service.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PermissionService permissionService;
    private final EventService eventService;
    private final GroupService groupService;
    private final UserService userService;
    private final EventRepository eventRepository;

    @GetMapping
    @Operation(summary = "get all admins")
    public List<AdminLightDto> getAdmins(Pageable pageable,
                                         @RequestParam(required = false) String fullName,
                                         @RequestHeader("user-id") Long userId) {
        if (!permissionService.hasPermission(userId, "view")) {
            return List.of();
        }
        return adminService.getAllAdmins(pageable, fullName);
    }

    @GetMapping("/{adminId}")
    @Operation(summary = "get admin by id")
    public AdminDto getAdmin(@PathVariable Long adminId, @RequestHeader("user-id") Long userId) {
        if (!permissionService.hasPermission(userId, "view")) {
            return null;
        }
        return adminService.getAdmin(adminId);
    }

    @PostMapping
    @Operation(summary = "create admin")
    public void createAdmin(@RequestBody AdminDto adminDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "create")) {
            adminService.saveAdmin(adminDto, userId);
        }
    }

    @PutMapping("/{adminId}")
    @Operation(summary = "update admin")
    public void updateAdmin(@PathVariable Long adminId,
                            @RequestBody AdminDto adminDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "updateAdmin")) {
            adminService.editAdmin(adminId, adminDto, userId);
        }
    }

    @PatchMapping("/{adminId}")
    @Operation(summary = "reset password")
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

    @PostMapping("/create-group")
    public void createGroup(@RequestBody AdminGroupRequestDto adminGroupRequestDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "create")) {
            adminService.createGroup(adminGroupRequestDto, userId);
        }
    }

    @PutMapping("/update-group/{id}")
    public void updateGroup(@RequestBody GroupUpdateDto groupUpdateDto, @RequestHeader("user-id") Long userId, @PathVariable("id") Long groupId) {
        if (permissionService.hasPermission(userId, "update")) {
            adminService.updateGroup(groupUpdateDto, userId, groupId);
        }
    }

    @DeleteMapping("/delete-group")
    public void deleteGroups(@RequestParam List<Long> groupIds, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "delete")) {
            adminService.deleteGroups(groupIds, userId);
        }
    }


    @PostMapping("/events")
    public void createEvent(@RequestBody EventDetailDto eventDetailDto, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "create")) {
            eventService.addEvent(eventDetailDto);
        }
    }

    @PutMapping("/events/update-event/{id}")
    public void updateEvent(@RequestBody EventDetailDto eventDetailDto, @PathVariable("id") Long eventId, @RequestHeader("user-id") Long userId) {
        if (permissionService.hasPermission(userId, "update")) {
            adminService.updateEvent(eventDetailDto, eventId, userId);
        }
    }

    @DeleteMapping("/events/delete-event/{id}")
    public void deleteEvent(@PathVariable Long id, @RequestHeader("user-id") Long userId){
        if(permissionService.hasPermission(userId, "delete")){
            eventRepository.deleteById(id);
        }
    }


}
