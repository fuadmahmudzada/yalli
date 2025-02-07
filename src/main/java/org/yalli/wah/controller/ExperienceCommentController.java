package org.yalli.wah.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.ExperienceCommentAddDto;
import org.yalli.wah.model.dto.ExperienceCommentDto;
import org.yalli.wah.model.dto.ExperienceDto;
import org.yalli.wah.service.ExperienceCommentService;

import java.util.List;

@RestController
@RequestMapping(("/v1/experiences/comments"))
public class ExperienceCommentController {

    private final ExperienceCommentService experienceCommentService;

    public ExperienceCommentController(ExperienceCommentService experienceCommentService) {
        this.experienceCommentService = experienceCommentService;
    }

    @PostMapping("/{link}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@RequestBody ExperienceCommentAddDto experienceCommentAddDto, Authentication authentication, @PathVariable String link, @RequestHeader("comment-id") Long commentId  ){
        experienceCommentService.addComment(experienceCommentAddDto, authentication, link, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExperienceCommentDto> getComments(@RequestHeader("comment-id") Long commentId,
                                                  @RequestHeader("experience-id") Long experienceId){
        return experienceCommentService.getComments(commentId, experienceId);
    }
}
