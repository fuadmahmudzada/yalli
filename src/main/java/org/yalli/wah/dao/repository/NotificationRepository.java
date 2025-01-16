package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yalli.wah.dao.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
    @Query("SELECT n FROM NotificationEntity n JOIN n.users u WHERE u.id = :userId or u.country = :country or n.forAllUsers = :forAllUsers")
    Optional<List<NotificationEntity>> findNotificationsForUser(@Param("userId") Long userId, @Param("forAllUsers") Boolean forAllUsers, @Param("country") String country);

}
