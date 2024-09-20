package org.yalli.wah.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.model.dto.CountryDto;
import org.yalli.wah.service.CountryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/countries")
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Get all countries")
    public List<CountryDto> getCountries() {
        return countryService.getCountries();
    }
}
