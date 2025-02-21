package org.yalli.wah.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;


import java.beans.ConstructorProperties;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_coordinate")
public class UserCoordinateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @OneToMany(mappedBy = "userCoordinate")
    private List<UserEntity> userEntity;

    public UserCoordinateEntity(Point location, List<UserEntity> userEntityList){
        this.location = location;
        this.userEntity = userEntityList;
    }
}
