package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yalli.wah.dao.entity.ExperienceCommentEntity;

import java.util.List;

public interface ExperienceCommentRepository extends JpaRepository<ExperienceCommentEntity, Long> {

    int countAllByExperienceCommentEntityNot(ExperienceCommentEntity experienceCommentEntity);

    int countAllByExperienceCommentEntity(ExperienceCommentEntity experienceCommentEntity);


    List<ExperienceCommentEntity> findAllByExperienceCommentEntity(ExperienceCommentEntity experienceCommentEntity);
}
