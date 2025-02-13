package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;
    @ManyToOne
    private ExperiencesEntity experiencesEntity;
    //parentId
    @ManyToOne
    @JoinColumn(unique = false)
    private ExperienceCommentEntity experienceCommentEntity;
}
