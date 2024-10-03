package org.yalli.wah.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.EventEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.EventRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.EventMapper;
import org.yalli.wah.model.dto.EventDto;
import org.yalli.wah.model.dto.EventSearchRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Page<EventDto> getAllEvents(EventSearchRequest eventSearchRequest, Pageable pageable, String token) {
        UserEntity userEntity = userRepository.findByAccessToken(token).orElse(null);
        Specification<EventEntity> spec = Specification.where((root, query, criteriaBuilder) -> {
            if (eventSearchRequest != null) {
                List<Predicate> predicates = new ArrayList<>();
                if (eventSearchRequest.getCategory() != null) {
                    switch (eventSearchRequest.getCategory()) {
                        case POPULAR:
                            predicates.add(criteriaBuilder.isTrue(root.get("isPopular")));
                            break;
                        case EXPIRED:
                            predicates.add(criteriaBuilder.lessThan(root.get("date"), LocalDate.now()));
                            break;
                        case UPCOMING:
                            predicates.add(criteriaBuilder.between(root.get("date"), LocalDate.now(), LocalDate.now().plusDays(7)));
                            break;
                        case SAVED:
                            if(userEntity != null) {
                                Join<EventEntity, UserEntity> userJoin = root.join("users", JoinType.INNER);
                                predicates.add(criteriaBuilder.equal(userJoin.get("id"), userEntity.getId()));
                            }
                    }
                }
                if (eventSearchRequest.getTitle() != null && !eventSearchRequest.getTitle().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(root.get("title"), "%" + eventSearchRequest.getTitle() + "%")
                    );
                }
                if (eventSearchRequest.getCountry() != null && !eventSearchRequest.getCountry().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.equal(root.get("country"), eventSearchRequest.getCountry())
                    );
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.conjunction();
            }
        });
        return eventRepository.findAll(spec, pageable).map(EventMapper.INSTANCE::mapEntityToDto);
    }
}

