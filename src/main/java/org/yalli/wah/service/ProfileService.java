package org.yalli.wah.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.ProfileMapper;
import org.yalli.wah.model.dto.MentorApplyDto;
import org.yalli.wah.model.dto.MentorRequestDto;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.PermissionException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final ReturnTypeParser genericReturnTypeParser;

    public MentorRequestDto getUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("USER_NOT_FOUND"));
        return ProfileMapper.INSTANCE.toMentorRequest(user);
    }

    public void addMentor(MentorApplyDto mentorApplyDto) {

        UserEntity userEntity = userRepository.findByEmail(mentorApplyDto.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("USER_NOT_FOUND"));
        mentorRepository.findByEmail(mentorApplyDto.getEmail()).ifPresent((mentor)-> {
            throw new InvalidInputException("MENTOR_EXISTS");
        });
        if (userEntity.getNotCompletedFields() != 0) {
            throw new PermissionException("USER_PROFILE_NOT_COMPLETE");
        }
        MentorEntity mentorEntity = ProfileMapper.INSTANCE.toMentorEntity(mentorApplyDto);
        mentorRepository.save(mentorEntity);

    }


}
