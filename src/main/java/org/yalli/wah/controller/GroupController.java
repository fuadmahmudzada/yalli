package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;
import org.yalli.wah.model.dto.GroupRequest;
import org.yalli.wah.model.dto.GroupSearchRequest;
import org.yalli.wah.model.dto.GroupUpdateDto;
import org.yalli.wah.service.GroupService;

import java.util.List;


@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
@CrossOrigin
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public Page<GroupLightDto> getAllGroupsLight(Pageable pageable,
                                                 @ModelAttribute GroupSearchRequest filter) {
        return groupService.getAllGroupsLight(pageable, filter);
    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    @PostMapping
    public void createGroup(@RequestBody GroupRequest groupRequest) {
        groupService.createGroup(groupRequest);
    }

    @GetMapping("/users/{userId}")
    public Page<GroupLightDto> getGroupsByUserId(@PathVariable Long userId, Pageable pageable) {
        return groupService.getGroupsByUserId(userId, pageable);
    }

    @GetMapping("{groupId}/users/{userId}")
    public GroupDto getGroupDtoByUserId(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.getGroupByUserId(groupId, userId);
    }

    @PutMapping("/{id}")
    public void updateGroup(@PathVariable Long id, @RequestBody GroupUpdateDto groupUpdateDto) {
        groupService.updateGroup(id, groupUpdateDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteGroup(@PathVariable Long userId, @RequestParam List<Long> groupIds) {
        groupService.deleteGroup(groupIds, userId);
    }
}
