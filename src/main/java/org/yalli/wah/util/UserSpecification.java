package org.yalli.wah.util;

import org.springframework.data.jpa.domain.Specification;
import org.yalli.wah.dao.entity.UserEntity;



public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                fullName == null || fullName.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<UserEntity> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                country == null || country.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), "%" + country.toLowerCase() + "%");
    }
}
