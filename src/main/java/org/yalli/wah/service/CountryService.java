package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.repository.CountryRepository;
import org.yalli.wah.model.dto.CountryDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryDto> getCountries() {
        return countryRepository.findAll().stream().map(c -> new CountryDto(c.getName())).toList();
    }
}
