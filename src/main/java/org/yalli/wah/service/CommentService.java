package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.mapper.CommentMapper;
import org.yalli.wah.model.dto.CommentAddDto;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;

    public void addComment(CommentAddDto commentAddDto) {
        log.info("ActionLog.addComment.start user {}",commentAddDto.getUserId());
        var username = userService.getUserById(commentAddDto.getUserId()).getFullName();
        commentRepository.save(commentMapper.mapCommentAddDtoToComment(commentAddDto, username));
        log.info("ActionLog.addComment.end user {}",commentAddDto.getUserId());
    }

}
