package org.yalli.wah.dao.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.yalli.wah.dao.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByAccessToken(String accessToken);
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);
    Optional<UserEntity> findAllByCountry(String country);

    boolean existsByEmail(String email);
}
