package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.model.enums.MentorStatus;

import java.util.Collection;
import java.util.Optional;


public interface MentorRepository extends JpaRepository<MentorEntity, Long>, JpaSpecificationExecutor<MentorEntity> {

    Optional<MentorEntity> findByUser_IdAndStatusIn(Long id, Collection<MentorStatus> statuses);

    Boolean existsByUser_Id(Long userId);
}
