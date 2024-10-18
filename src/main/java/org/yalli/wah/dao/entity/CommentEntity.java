package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String rate;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "mentors_id")
    private MentorEntity mentor;
    @CreationTimestamp
    private Date createdAt;
}
