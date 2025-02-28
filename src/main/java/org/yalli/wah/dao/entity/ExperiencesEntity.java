package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExperiencesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageLink;
    private String link;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;
    @OneToMany(mappedBy = "experiencesEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperienceCommentEntity> experienceCommentEntity;

}
