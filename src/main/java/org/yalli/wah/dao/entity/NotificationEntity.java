package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String detail;
    @CreationTimestamp
    private LocalDateTime sentTime;
    @ManyToMany(mappedBy="notifications")
    private List<UserEntity> users;
    private Boolean forAllUsers=false;
}
