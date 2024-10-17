package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.mapper.CommentMapper;
import org.yalli.wah.model.dto.CommentAddDto;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void addComment(CommentAddDto commentAddDto) {


        commentRepository.save(commentMapper.mapCommentAddDtoToComment(commentAddDto));
    }

}
