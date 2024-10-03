package org.yalli.wah.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.enums.MentorCategory;
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
    public Page<MentorSearchDto> search(@RequestParam(required = false) String fullName,
                                        @RequestParam(required = false) String country,
                                        @RequestParam(required = false) MentorCategory mentorCategory,
                                        Pageable pageable) {
        return mentorService.searchMembers(fullName, country, mentorCategory, pageable);
    }
}

