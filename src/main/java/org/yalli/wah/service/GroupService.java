package org.yalli.wah.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.dao.repository.GroupRepository;
import org.yalli.wah.mapper.GroupMapper;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;
import org.yalli.wah.model.dto.GroupRequest;
import org.yalli.wah.model.dto.GroupSearchRequest;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupService.class);
    private final GroupRepository groupRepository;
    private final MinioService minioService;

    public List<GroupLightDto> getAllGroupsLight(Pageable pageable, GroupSearchRequest groupSearchRequest) {
        Specification<GroupEntity> specification = Specification.where((root, query, criteriaBuilder) -> {
            if (groupSearchRequest != null) {
                List<Predicate> predicates = new ArrayList<>();
                if (groupSearchRequest.getGroupCategory() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("category"), groupSearchRequest.getGroupCategory().name())
                    );
                }
                if (groupSearchRequest.getTitle() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("title"), groupSearchRequest.getTitle())
                    );
                }
                if (groupSearchRequest.getCountry() != null) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("country"), groupSearchRequest.getCountry())
                    );
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
            } else {
                return criteriaBuilder.conjunction();
            }
        });

        return GroupMapper.INSTANCE.mapEntitiesToGroupLightDtos(groupRepository.findAll(pageable).getContent());
    }

    public GroupDto getGroupById(Long id) {
        return GroupMapper.INSTANCE.mapEntityToDto(groupRepository.findById(id).orElseThrow(() ->
        {
            log.error("ActionLog.getGroupById.error group not found with id {}", id);
            return new ResourceNotFoundException("GROUP_NOT_FOUND");
        }));
    }

    public GroupDto createGroup(GroupRequest groupDto, MultipartFile multipartFile) {
        log.info("ActionLog.createGroup.start groupDto {}", groupDto);
        minioService.uploadFile();

    }
}
