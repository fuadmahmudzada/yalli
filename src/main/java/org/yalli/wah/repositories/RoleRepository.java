package org.yalli.wah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yalli.wah.models.Role;
import org.yalli.wah.models.User;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllById(Iterable<Long> ids);
    Optional<Role> findByName(String name);
}
