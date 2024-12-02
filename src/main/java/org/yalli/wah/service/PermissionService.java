package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.AdminEntity;
import org.yalli.wah.dao.repository.AdminRepository;
import org.yalli.wah.model.enums.Role;
import org.yalli.wah.model.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final AdminRepository adminRepository;

    public boolean hasPermission(Long id, String operation) {
        AdminEntity adminEntity = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ADMIN_NOT_FOUND"));

        if (adminEntity.getRole() == Role.SUPER_ADMIN) {

            return true;

        } else {
            return adminEntity.getRole().getOperations().contains(operation);
        }
    }

}
