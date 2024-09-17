package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.domain.Specification;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.enums.MentorCategory;

public class MentorSpecification {

    public static Specification<MentorEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) ->

                criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<MentorEntity> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }

    public static Specification<MentorEntity> containsCategory(MentorCategory category) {

        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mentorCategory"), category));
    }

}
