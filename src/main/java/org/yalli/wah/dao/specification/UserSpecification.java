package org.yalli.wah.dao.specification;

import org.springframework.data.jpa.domain.Specification;
import org.yalli.wah.dao.entity.UserEntity;

import java.util.List;


public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                fullName == null || fullName.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), fullName.toLowerCase() + "%");
    }

    public static Specification<UserEntity> hasCountry(List<String> countryList) {
        return (root, query, criteriaBuilder) ->
                countryList == null || countryList.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.lower(root.get("country")).in(countryList.stream().map(String::toLowerCase).toList());
    }

    public static Specification<UserEntity> isEmailConfirmed() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isTrue(root.get("emailConfirmed"));
    }

    public static Specification<UserEntity> hasCity(List<String> cityList) {
        return ((root, query, criteriaBuilder) ->
                cityList == null || cityList.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.lower(root.get("city")).in(cityList.stream().map(String::toLowerCase).toList()));
    }

    public static Specification<UserEntity> isProfilePictureNotNull() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("profilePictureUrl"));
    }
}
