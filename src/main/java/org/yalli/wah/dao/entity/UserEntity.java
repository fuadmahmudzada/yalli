package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.yalli.wah.model.enums.SocialMedia;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String country;
    private String city;
    private String accessToken;
    private LocalDateTime tokenExpire;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String profilePictureUrl;
    @ManyToMany
    @JoinTable(
            name = "user_saved_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<EventEntity> savedEvents;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "account_urls")
    private Map<SocialMedia, String> socialMediaAccounts = new HashMap<>();
    private String otp;
    private LocalDateTime otpExpiration;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean otpVerified;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean emailConfirmed = false;
}
