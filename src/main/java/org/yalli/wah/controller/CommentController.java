package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.model.dto.CommentAddDto;
import org.yalli.wah.service.CommentService;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public void addComment(@RequestBody CommentAddDto commentAddDto){
        commentService.addComment(commentAddDto);
    }
}
