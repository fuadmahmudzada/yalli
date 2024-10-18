package org.yalli.wah.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.dao.repository.MentorSpecification;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.CommentMapper;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.mapper.MentorMapper;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {
    private static final Logger log = LoggerFactory.getLogger(MentorService.class);
    private final MentorRepository mentorRepository;
    private final CommentRepository commentRepository;

    private final MentorMapper mentorMapper;

    public Page<MentorSearchDto> searchMembers(String fullName, String country, List<MentorCategory> mentorCategory, Pageable pageable) {

        Specification<MentorEntity> specification1 = Specification.where(null);
        if (StringUtils.hasLength(fullName)) {
            specification1 = specification1.and(MentorSpecification.hasFullName(fullName));
        }

        if (StringUtils.hasLength(country)) {
            specification1 = specification1.and(MentorSpecification.hasCountry(country));
        }
//bunu morterizesiz sadelesdirmek

        Specification<MentorEntity> specification = Specification.where(null);
        System.out.println(mentorCategory);
        if (mentorCategory != null) {
            for(int i=0;i<mentorCategory.size();i++){
                if(i==0){
                    specification = specification.or(MentorSpecification.containsCategory(mentorCategory.get(i)));
                }
                else{
                    specification = specification.or(MentorSpecification.containsCategory(mentorCategory.get(i)));
                }

            }

        }




        Page<MentorEntity> mentorEntities = mentorRepository.findAll(specification1.and(specification), pageable);
        return mentorEntities.map(mentorMapper::mapMentorEntityToMentorDto);
    }

    public MentorDetailDto getMentorById(Long id, Pageable pageable) {
        commentRepository.findAllByMentorId(id);
        return mentorMapper.mapMentorToMentorDetailDto(mentorRepository.findById(id).orElseThrow(()
        -> new ResourceNotFoundException("MENTOR_ENTITY_NOT_FOUND")), pageable);
    }
}
