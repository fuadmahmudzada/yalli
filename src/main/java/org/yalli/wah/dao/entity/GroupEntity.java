package org.yalli.wah.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.yalli.wah.model.enums.GroupCategory;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 10000)
    private String description;
    @Column(columnDefinition = "TEXT")
    private String about;
    private String imageId;
    private String country;
    private Short renameCount;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> gallery;
    private Long memberCount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    private String link;
    @Enumerated(EnumType.STRING)
    private GroupCategory category;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}