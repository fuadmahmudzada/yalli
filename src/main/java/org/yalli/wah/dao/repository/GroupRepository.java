package org.yalli.wah.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.yalli.wah.dao.entity.GroupEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {
    Page<GroupEntity> findByUserEntity_Id(Long id, Pageable pageable);
    Optional<GroupEntity> findByIdAndUserEntity_Id(Long id, Long id1);
    long deleteByUserEntity_IdAndIdIn(Long id, Collection<Long> ids);
}
