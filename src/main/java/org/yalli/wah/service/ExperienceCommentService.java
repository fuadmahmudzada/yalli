package org.yalli.wah.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.ExperienceCommentEntity;
import org.yalli.wah.dao.entity.ExperiencesEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.ExperienceCommentRepository;
import org.yalli.wah.dao.repository.ExperiencesRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.ExperienceCommentMapper;
import org.yalli.wah.model.dto.ExperienceCommentAddDto;
import org.yalli.wah.model.dto.ExperienceCommentDto;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ExperienceCommentService {


    private final ExperienceCommentRepository experienceCommentRepository;
    private final UserRepository userRepository;
    private final ExperiencesRepository experiencesRepository;

    public ExperienceCommentService(ExperienceCommentRepository experienceCommentRepository, UserRepository userRepository, ExperiencesRepository experiencesRepository) {
        this.experienceCommentRepository = experienceCommentRepository;
        this.userRepository = userRepository;
        this.experiencesRepository = experiencesRepository;
    }

    public void addComment(ExperienceCommentAddDto experienceCommentAddDto, Authentication authentication, String link, Long commentId){
        log.info("ActionLog.addMethod.start with {} ", experienceCommentAddDto);
        UserEntity user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->
                new ResourceNotFoundException("UserEntity couldn't found with email " + authentication.getName()));
        ExperiencesEntity experiencesEntity = experiencesRepository.findByLink(link).orElseThrow(()->
                new ResourceNotFoundException("Experience didn't find with "+link));
        ExperienceCommentEntity experienceCommentEntity = new ExperienceCommentEntity();
        if(commentId==-1){
            experienceCommentEntity = null;
        } else{
            experienceCommentEntity.setId(commentId);
        }

        experienceCommentRepository.save(ExperienceCommentMapper.INSTANCE.toEntity(experienceCommentAddDto, user, experiencesEntity, experienceCommentEntity));
    }


    public List<ExperienceCommentDto> toList(List<ExperienceCommentEntity> experienceCommentEntity){
        List<ExperienceCommentDto> commentDtoList = new ArrayList<>();
        AtomicInteger replyCount = new AtomicInteger();
        experienceCommentEntity.forEach(commentEntity-> {
            replyCount.set(experienceCommentRepository.countAllByExperienceCommentEntity(commentEntity));
            commentDtoList.add( ExperienceCommentMapper.INSTANCE.toExperienceCommentDto(commentEntity, replyCount.get()));
        });
        return commentDtoList;
    }
    public List<ExperienceCommentDto> getComments(Long id){
        log.info("ActionLog.getComments.start");
        ExperienceCommentEntity experienceCommentEntity = new ExperienceCommentEntity();
        if(id==-1){
            experienceCommentEntity = null;
        } else{
            experienceCommentEntity.setId(id);
        }
//her brin comment in ozunun id sini parent id ye qoyub say tapmaliyam
//        int replyCount = experienceCommentRepository.countAllByExperienceCommentEntity(experienceCommentEntity);

        return toList(experienceCommentRepository.findAllByExperienceCommentEntity(experienceCommentEntity));
    }
}
