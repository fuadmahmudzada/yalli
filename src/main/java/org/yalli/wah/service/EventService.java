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
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.annotation.Transactional;
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
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private static final Locale AZERBAIJANI =  Locale.forLanguageTag("az");
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


            List<Predicate> answer = new ArrayList<>();
            if (eventSearchRequest.getTitle() != null && !eventSearchRequest.getTitle().isEmpty()) {
                answer.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), eventSearchRequest.getTitle() + "%"));
            }

            List<Predicate> otherPredicates = new ArrayList<>();

            if (eventSearchRequest.getCountry() != null && !eventSearchRequest.getCountry().isEmpty()) {
                otherPredicates.add(criteriaBuilder.or(criteriaBuilder.lower(root.get("country"))
                        .in(eventSearchRequest.getCountry().stream()
                                .map(country->country.toLowerCase(AZERBAIJANI))
                                .collect(Collectors.toList())
                        )));
            }

            if (eventSearchRequest.getCity() != null && !eventSearchRequest.getCity().isEmpty()) {
                List<String> lowerCaseCities = eventSearchRequest.getCity().stream()
                        .map(city->city.toLowerCase(AZERBAIJANI))
                        .toList();
                otherPredicates.add(criteriaBuilder.or(
                        criteriaBuilder.lower(root.get("city")).in(lowerCaseCities))
                );
            }
            if (!otherPredicates.isEmpty()) {
                if (!answer.isEmpty()) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.or(otherPredicates.toArray(new Predicate[0])), answer.getFirst()));
                } else{
                    predicates.add(criteriaBuilder.and(criteriaBuilder.or(otherPredicates.toArray(new Predicate[0]))));
                }
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
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

    @Transactional
    public void saveEvent(EventSaveDto eventSaveDto) {
        EventEntity eventEntity = eventRepository.findById(eventSaveDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event_NOT_FOUND"));

        UserEntity userEntity = userRepository.findById(eventSaveDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));

        if (!userEntity.getSavedEvents().contains(eventEntity)) {
            userEntity.getSavedEvents().add(eventEntity);
            eventEntity.getUsers().add(userEntity);
            userRepository.save(userEntity);
            eventRepository.save(eventEntity);
        } else{
            throw new InvalidInputException("User Have Already Saved Event");
        }
    }

    @Transactional
    public void unsaveEvent(EventSaveDto eventSaveDto) {
        log.info("ActionLog.unsaveEvent.start with dto {}", eventSaveDto);
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
        log.info("ActionLog.unsaveEvent.end with dto {}", eventSaveDto);
    }

    public void addEvent(EventDetailDto eventDetailDto) {
        eventRepository.save(EventMapper.INSTANCE.mapDtoToEntity(eventDetailDto));
    }
}

