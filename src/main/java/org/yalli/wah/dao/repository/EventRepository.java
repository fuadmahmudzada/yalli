package org.yalli.wah.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.yalli.wah.dao.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
//    @Query("SELECT u from EventEntity e JOIN e.users users WHERE e.id= :userId AND coupon.id= :couponId")
//    List<EventEntity> findEvents;
}
