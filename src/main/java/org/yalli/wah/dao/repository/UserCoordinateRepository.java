package org.yalli.wah.dao.repository;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yalli.wah.dao.entity.UserCoordinateEntity;

import java.util.List;
import java.util.Optional;

public interface UserCoordinateRepository extends JpaRepository<UserCoordinateEntity, Long> {
    @Query(nativeQuery = true,value = "SELECT distinct on(location) id, location FROM user_coordinate WHERE ST_WITHIN(location, ?1) ORDER BY location")
    List<UserCoordinateEntity> findAllWithinGivenPolygon(Polygon polygon);
}
