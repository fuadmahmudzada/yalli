package org.yalli.wah.service;


import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.mapper.MentorMapper;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchRequest;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.model.enums.MentorStatus;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {
    private static final Logger log = LoggerFactory.getLogger(MentorService.class);
    private final MentorRepository mentorRepository;
    private final CommentRepository commentRepository;

    public Page<MentorSearchDto> searchMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        Specification<MentorEntity> specification = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (mentorSearchRequest.getFullName() != null && !mentorSearchRequest.getFullName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + mentorSearchRequest.getFullName() + "%"));
            }
            if (mentorSearchRequest.getCategory() != null && !mentorSearchRequest.getCategory().isEmpty()) {
                predicates.add(root.get("category").in(mentorSearchRequest.getCategory()));
            }
            if (mentorSearchRequest.getCountry() != null && !mentorSearchRequest.getCountry().isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("country"), mentorSearchRequest.getCountry())
                );
            }
            predicates.add(criteriaBuilder.equal(root.get("mentorStatus"), MentorStatus.ACCEPTED));
            predicates.add(criteriaBuilder.conjunction());
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return mentorRepository.findAll(specification, pageable).map(MentorMapper.INSTANCE::mapMentorEntityToMentorSearchDto);
    }

    public MentorDetailDto getMentorById(Long id) {
        commentRepository.findAllByMentorId(id);
        var mentor = mentorRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("MENTOR_ENTITY_NOT_FOUND"));
        return MentorMapper.INSTANCE.mapMentorToMentorDetailDto(mentor, calcAverageRating(mentor.getComments()));
    }

    private Double calcAverageRating(List<CommentEntity> comments) {
        return comments.stream().map(CommentEntity::getRate).mapToInt(Integer::intValue).average().orElse(0);
    }
}
