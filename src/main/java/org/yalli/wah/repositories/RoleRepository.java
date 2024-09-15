package org.yalli.wah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yalli.wah.models.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllById(Iterable<Long> ids);

}
