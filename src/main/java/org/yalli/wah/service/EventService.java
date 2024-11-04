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
import org.yalli.wah.model.dto.EventDetailDto;
import org.yalli.wah.model.dto.EventDto;
import org.yalli.wah.model.dto.EventSaveDto;
import org.yalli.wah.model.dto.EventSearchRequest;
import org.yalli.wah.model.enums.EventCategory;
import org.yalli.wah.model.exception.ResourceNotFoundException;

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
        UserEntity userEntity;
        if (token != null) {
            userEntity = userRepository.findByAccessToken(token).orElse(null);
        } else {
            userEntity = null;
        }
        Specification<EventEntity> spec = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (eventSearchRequest.getCategory() != null && !eventSearchRequest.getCategory().isEmpty()) {
                List<Predicate> categoryPredicates = new ArrayList<>();
                for (EventCategory category : eventSearchRequest.getCategory()) {
                    switch (category) {
                        case EXPIRED:
                            categoryPredicates.add(criteriaBuilder.lessThan(root.get("date"), LocalDate.now()));
                            break;

                        case UPCOMING:
                            categoryPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), LocalDate.now()));
                            break;

                        case POPULAR:
                            categoryPredicates.add(criteriaBuilder.isTrue(root.get("isPopular")));
                            break;

                        case SAVED:
                            if (userEntity != null) {
                                Join<EventEntity, UserEntity> userJoin = root.join("users", JoinType.INNER);
                                categoryPredicates.add(criteriaBuilder.equal(userJoin.get("id"), userEntity.getId()));
                            }
                            break;
                    }
                }

                predicates.add(criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0])));
            }


            if (eventSearchRequest.getTitle() != null && !eventSearchRequest.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + eventSearchRequest.getTitle() + "%"));
            }


            if (eventSearchRequest.getCountry() != null && !eventSearchRequest.getCountry().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("country"), eventSearchRequest.getCountry()));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });


        return eventRepository.findAll(spec, pageable).map(it -> EventMapper.INSTANCE.mapEntityToDto(it,
                userEntity == null ? null : userEntity.getId()));
    }

    public EventDetailDto getEventById(Long id) {
        return EventMapper.INSTANCE.manEntityToEventDetailDto(eventRepository.findById(id).orElseThrow(() ->
        {
            log.error("ActionLog.getEventById.error event not found with id {}", id);
            return new ResourceNotFoundException("EVENT_NOT_FOUND");
        }));
    }

    public void saveEvent(EventSaveDto eventSaveDto) {
        EventEntity eventEntity = eventRepository.findById(eventSaveDto.getId()).orElseThrow(() ->
        {
            log.error("ActionLog.findById.error event not found with id {}", eventSaveDto.getId());
            return new ResourceNotFoundException("Event_NOT_FOUND");
        });
        UserEntity userEntity = userRepository.findById(eventSaveDto.getUserId()).orElseThrow(() -> {
            log.error("ActionLog.findById.error user not found with id {}", eventSaveDto.getUserId());
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        List<EventEntity> userEvents = userEntity.getSavedEvents();
        userEvents.add(eventEntity);
        userEntity.setSavedEvents(userEvents);
        userRepository.save(userEntity);

    }

    public void unsaveEvent(EventSaveDto eventSaveDto) {
        EventEntity eventEntity = eventRepository.findById(eventSaveDto.getId()).orElseThrow(() ->
        {
            log.error("ActionLog.findById.error event not found with id {}", eventSaveDto.getId());
            return new ResourceNotFoundException("EVENT_NOT_FOUND");
        });
        UserEntity userEntity = userRepository.findById(eventSaveDto.getUserId()).orElseThrow(() ->
        {
            log.error("ActionLog.findById.error user not found with id {}", eventSaveDto.getUserId());
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        List<EventEntity> savedEntities = userEntity.getSavedEvents();
        savedEntities.remove(eventEntity);
        userEntity.setSavedEvents(savedEntities);
        userRepository.save(userEntity);
    }
}

