package org.yalli.wah.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.dao.repository.MentorSpecification;
import org.yalli.wah.enums.MentorCategory;
import org.yalli.wah.mapper.MentorMapper;
import org.yalli.wah.model.dto.MentorSearchDto;

@Service
@RequiredArgsConstructor
public class MentorService {
    private final MentorRepository mentorRepository;

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
}
