package org.yalli.wah.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yalli.wah.dao.entity.ExperiencesEntity;

import java.util.List;
import java.util.Optional;

public interface ExperiencesRepository extends JpaRepository<ExperiencesEntity, Long> {
    Page<ExperiencesEntity> findAll(Specification<ExperiencesEntity> specification, Pageable pageable);
    Optional<ExperiencesEntity> findByLink(String link);

    List<ExperiencesEntity> findAllByUserEntity_Id(Long userEntityId);
    List<ExperiencesEntity> findAllByUserEntity_Email(String userEntityEmail);
}
