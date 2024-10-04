package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;
import org.yalli.wah.model.dto.GroupRequest;
import org.yalli.wah.model.dto.GroupSearchRequest;
import org.yalli.wah.service.GroupService;


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
    public void createGroup(@RequestPart("data") GroupRequest groupRequest, @RequestPart("image") MultipartFile image) {
        groupService.createGroup(groupRequest, image);
    }
}
