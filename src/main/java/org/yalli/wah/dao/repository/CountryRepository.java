package org.yalli.wah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yalli.wah.dao.entity.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity,Long> {
}
