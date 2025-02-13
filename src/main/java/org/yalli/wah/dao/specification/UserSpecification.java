package org.yalli.wah.dao.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.yalli.wah.dao.entity.UserEntity;

import java.util.List;


public class UserSpecification {

    public static Specification<UserEntity> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), fullName.toLowerCase() + "%");
    }

    public static Specification<UserEntity> hasCountryOrCity(List<String> countryList, List<String> cityList) {

        return (root, query, criteriaBuilder) -> {
            Predicate finalPredicate = null;
            if(countryList != null && !countryList.isEmpty()) {
                finalPredicate= criteriaBuilder.lower(root.get("country")).in(countryList.stream().map(String::toLowerCase).toList());
            }
            if(cityList != null && !cityList.isEmpty()) {
                Predicate cityPredicate =  criteriaBuilder.lower(root.get("city")).in(cityList.stream().map(String::toLowerCase).toList());
                if (finalPredicate != null) {

                    criteriaBuilder.or(cityPredicate,finalPredicate );

                } else {
                    finalPredicate = cityPredicate;
                }

            }
            return finalPredicate;
        };
    }

    public static Specification<UserEntity> isEmailConfirmed() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isTrue(root.get("emailConfirmed"));
    }


    public static Specification<UserEntity> isProfilePictureNotNull() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("profilePictureUrl"));
    }
}
