package org.yalli.wah.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.dao.repository.MentorSpecification;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.mapper.MentorMapper;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.model.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class MentorService {
    private static final Logger log = LoggerFactory.getLogger(MentorService.class);
    private final MentorRepository mentorRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
//    private final MentorMapper mentorMapper;

    public Page<MentorSearchDto> searchMembers(String fullName, String country, MentorCategory mentorCategory, Pageable pageable) {
        Specification<MentorEntity> specification = Specification.where(null);

        if (StringUtils.hasLength(fullName)) {
            specification = specification.and(MentorSpecification.hasFullName(fullName));
        }

        if (StringUtils.hasLength(country)) {
            specification = specification.and(MentorSpecification.hasCountry(country));
        }

        if (mentorCategory != null) {
            specification = specification.and(MentorSpecification.containsCategory(mentorCategory));
        }

        Page<MentorEntity> mentorEntities = mentorRepository.findAll(specification, pageable);

        return mentorEntities.map(MentorMapper.INSTANCE::mapMentorEntityToMentorDto);
    }

//    public Page<MentorDetailDto> getMentorById(Long id, int number, int size ) {
//
//        Pageable pageable = PageRequest.of(number, size);
//        MentorEntity mentorEntities = mentorRepository.findById(id).orElseThrow();
//        mentorEntities.setComments(commentRepository.findAll(pageable));
//        mentorRepository.
//        return mentorEntities.map(MentorMapper.INSTANCE::mapMentorToMentorDetailDto);
////return mentorMapper.mapMentorToMentorDetailDto(mentorRepository.findAll(pageable));
//    }
}
