package org.yalli.wah.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchRequest;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.service.MentorService;


@RestController
@RequestMapping("/v1/mentors")
@CrossOrigin
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping("/search")
    @Operation(summary = "search mentors")
    @ResponseStatus(HttpStatus.OK)
    public Page<MentorSearchDto> search(@ModelAttribute MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        return mentorService.searchMentors(mentorSearchRequest, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get mentor with its info and comments")
    public MentorDetailDto getMentor(@PathVariable Long id) {
        return mentorService.getMentorById(id);
    }
}

