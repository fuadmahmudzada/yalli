package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yalli.wah.dao.entity.NotificationEntity;
import org.yalli.wah.dao.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
    Optional<List<NotificationEntity>> findAllByUsersId(Long users_id);
}
