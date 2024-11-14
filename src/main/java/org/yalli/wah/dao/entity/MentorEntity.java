package org.yalli.wah.dao.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "mentors")
@NoArgsConstructor
@AllArgsConstructor
public class MentorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String country;
    private String city;
    private String email;
    private String profilePicture;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    private MentorCategory mentorCategory;
    @OneToMany(mappedBy = "mentor")
    private List<CommentEntity> comments;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String link;
    @Enumerated(EnumType.STRING)
    private MentorStatus mentorStatus = MentorStatus.ON_HOLD;
}
