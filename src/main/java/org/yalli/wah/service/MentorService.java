package org.yalli.wah.service;


import ch.qos.logback.classic.spi.IThrowableProxy;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.ExperiencesEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.repository.CommentRepository;
import org.yalli.wah.dao.repository.ExperiencesRepository;
import org.yalli.wah.dao.repository.MentorRepository;
import org.yalli.wah.mapper.ExperiencesMapper;
import org.yalli.wah.mapper.MentorMapper;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.enums.MentorStatus;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.yalli.wah.model.enums.EmailTemplate.MENTORSHIP_ACCEPT;
import static org.yalli.wah.model.enums.EmailTemplate.MENTORSHIP_APPLY;
import static org.yalli.wah.model.enums.EmailTemplate.MENTORSHIP_REJECT;

@Service
@RequiredArgsConstructor
public class MentorService {
    private static final Logger log = LoggerFactory.getLogger(MentorService.class);
    private final MentorRepository mentorRepository;
    private final CommentRepository commentRepository;
    private final EmailService emailService;
    private final UserService userService;
    private static final Locale AZERBAIJANI =  Locale.forLanguageTag("az");
    private final ExperiencesRepository experiencesRepository;

    public Page<MentorSearchDto> searchMentors(MentorSearchRequest mentorSearchRequest, Pageable pageable) {
        Specification<MentorEntity> specification = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (mentorSearchRequest.getFullName() != null && !mentorSearchRequest.getFullName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("fullName")),
                        mentorSearchRequest.getFullName().toLowerCase() + "%"));
            }
            if (mentorSearchRequest.getCategory() != null && !mentorSearchRequest.getCategory().isEmpty()) {
                predicates.add(root.get("mentorCategory").in(mentorSearchRequest.getCategory()));
            }
            List<Predicate> mentorLocPredicates = new ArrayList<>();
            if (mentorSearchRequest.getCountry() != null && !mentorSearchRequest.getCountry().isEmpty()) {
                mentorLocPredicates.add(
                        criteriaBuilder.lower(root.get("user").get("country")).in(mentorSearchRequest.getCountry()
                                .stream().map(country-> country.toLowerCase(AZERBAIJANI)).collect(Collectors.toList()))
                );
            }

            List<String> list = new ArrayList<>();
            list.add("");
            if (mentorSearchRequest.getCity() != null && !mentorSearchRequest.getCity().isEmpty()) {
                mentorLocPredicates.add(
                        criteriaBuilder.lower(root.get("user").get("city")).in(mentorSearchRequest.getCity()
                                .stream().map(city->city.toLowerCase(AZERBAIJANI)).collect(Collectors.toList()))
                );
            }
            if (!mentorLocPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(mentorLocPredicates.toArray(new Predicate[0])));
            }
            predicates.add(criteriaBuilder.equal(root.get("status"), MentorStatus.ACCEPTED));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return mentorRepository.findAll(specification, pageable).map(x-> {
            int avgRating = 0;
            for(CommentEntity commentEntity: x.getComments()){
                avgRating+=commentEntity.getRate();
            }
            avgRating = !x.getComments().isEmpty() ? (byte)avgRating/x.getComments().size() : 0;
            return MentorMapper.INSTANCE.mapMentorEntityToMentorSearchDto(x,
                    x.getComments().size(),
                    (byte)avgRating);
        });
    }

    public MentorDetailDto getMentorById(Long id) {
        commentRepository.findAllByMentorId(id);
        var mentor = mentorRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("MENTOR_ENTITY_NOT_FOUND"));
        return MentorMapper.INSTANCE.mapMentorToMentorDetailDto(mentor, calcAverageRating(mentor.getComments()));
    }

    public void applyToMentorship(MentorshipDto mentorshipDto) {
        log.info("ActionLog.applyToMentorship.start for user {}", mentorshipDto.getUserId());
        mentorRepository.findByUser_IdAndStatusIn(mentorshipDto.getUserId(),
                List.of(MentorStatus.REVIEW, MentorStatus.APPLIED)).ifPresent(mentor -> {
            log.warn("ActionLog.applyToMentorship.warn user {} already applied", mentorshipDto.getUserId());
            throw new InvalidInputException("USER_ALREADY_REQUESTED");
        });
        var user = userService.getUserById(mentorshipDto.getUserId());
        MentorEntity mentorEntity = MentorMapper.INSTANCE.mapMentorshipDtoToEntity(mentorshipDto);
        mentorRepository.save(mentorEntity);
        log.info("ActionLog.applyToMentorship.end for user {}", mentorshipDto.getUserId());
        emailService.sendMail(user.getEmail(), MENTORSHIP_APPLY.getSubject(), MENTORSHIP_APPLY.getBody());
    }

    public void assignRequest(Long mentorId) {
        log.info("ActionLog.assignRequest.start for mentor {}", mentorId);
        var entity = getMentorEntity(mentorId);
        entity.setStatus(MentorStatus.REVIEW);
        mentorRepository.save(entity);
        log.info("ActionLog.assignRequest.end for mentor {}", mentorId);
    }


    public void rejectMentorship(Long mentorId) {
        log.info("ActionLog.rejectMentorship.start");
        var entity = getMentorEntity(mentorId);
        entity.setStatus(MentorStatus.REJECTED);
        mentorRepository.save(entity);
        log.info("ActionLog.rejectMentorship.end");
        emailService.sendMail(entity.getUser().getEmail(), MENTORSHIP_REJECT.getSubject(), MENTORSHIP_REJECT.getBody());
    }

    public void acceptMentorship(Long mentorId) {
        log.info("ActionLog.acceptMentorship.start");
        var entity = getMentorEntity(mentorId);
        entity.setStatus(MentorStatus.ACCEPTED);
        mentorRepository.save(entity);
        log.info("ActionLog.acceptMentorship.end");
        emailService.sendMail(entity.getUser().getEmail(), MENTORSHIP_ACCEPT.getSubject(), MENTORSHIP_ACCEPT.getBody());
    }

    private Double calcAverageRating(List<CommentEntity> comments) {
        return comments.stream().map(CommentEntity::getRate).mapToInt(Integer::intValue).average().orElse(0);
    }

    private MentorEntity getMentorEntity(Long mentorId) {
        return mentorRepository.findById(mentorId).orElseThrow(() -> new ResourceNotFoundException("MENTOR_ENTITY_NOT_FOUND"));
    }

    public HashMap<String, Float> getMentorServices(Long id){
        MentorEntity mentorEntity = mentorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("MENTOR WITH " + id + " DOESN'T EXIST"));
        System.out.println(mentorEntity.toString());
        System.out.println(mentorEntity.getServices());
        return mentorEntity.getServices();
    }

    public List<ExperienceDto> getMentorExps(Long id){
        List<ExperiencesEntity> experiencesEntityList = experiencesRepository.findAllByUserEntity_Id(id);
        return experiencesEntityList.stream().map(ExperiencesMapper.INSTANCE::toDto).toList();
    }
    public List<MentorAdminDto> getAllMentors(){
        List<MentorEntity> mentorEntities =  mentorRepository.findAll();
        return mentorEntities.stream().map(MentorMapper.INSTANCE::mapMentorToMentorAdminDto).toList();
    }

    public void deleteMentor(Long id){
         mentorRepository.deleteById(id);
    }
}
