package org.yalli.wah.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.model.dto.MentorDetailDto;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final CommentRepository commentRepository;

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("USER ENTITY NOT FOUND"));
    }


    public MentorEntity findMentorById(Long id) {
        return mentorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MENTOR ENTITY NOT FOUND"));
    }
//     private List<CommentEntity> comments; private List<CommentDto> comments;
    //   private UserEntity users;   private String userName;

//    public String toName(UserEntity userEntity) {
//        return userEntity.getFullName();
//    }
}
