package org.yalli.wah.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.MentorApplyDto;
import org.yalli.wah.model.dto.MentorRequestDto;
import org.yalli.wah.service.ProfileService;

@RestController
@RequestMapping("v1/board")
@CrossOrigin
@RequiredArgsConstructor
public class BoardController {

    private final ProfileService profileService;

    @GetMapping("/mentorship/apply/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get initial user info for mentor apply")
    public MentorRequestDto getUser(@PathVariable Long id) {
        return profileService.getUser(id);
    }

    @PostMapping("/mentorship/apply")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "apply mentorship")
    public void addMentor(@RequestBody MentorApplyDto mentorApplyDto) {
        profileService.addMentor(mentorApplyDto);
    }

}
