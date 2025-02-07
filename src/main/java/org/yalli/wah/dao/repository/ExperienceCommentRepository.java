package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yalli.wah.dao.entity.ExperienceCommentEntity;
import org.yalli.wah.dao.entity.ExperiencesEntity;

import java.util.List;

public interface ExperienceCommentRepository extends JpaRepository<ExperienceCommentEntity, Long> {

    int countAllByExperienceCommentEntityNot(ExperienceCommentEntity experienceCommentEntity);

    int countAllByExperienceCommentEntity(ExperienceCommentEntity experienceCommentEntity);


    List<ExperienceCommentEntity> findAllByExperienceCommentEntity(ExperienceCommentEntity experienceCommentEntity);

    List<ExperienceCommentEntity> findAllByExperienceCommentEntityIdAndExperiencesEntityId(Long experienceCommentEntity_id, Long experiencesEntity_id);
}
