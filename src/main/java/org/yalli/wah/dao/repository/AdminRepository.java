package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.yalli.wah.dao.entity.AdminEntity;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> , JpaSpecificationExecutor<AdminEntity> {

    Optional<AdminEntity> findByUsernameOrEmail(String username, String email);
    Optional<AdminEntity> findByEmail(String email);
}
