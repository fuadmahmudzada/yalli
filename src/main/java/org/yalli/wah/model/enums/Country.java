package org.yalli.wah.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Country {
    SINGAPORE("Singapore"),
    AZERBAIJAN("Azerbaijan"),
    POLAND("Poland"),
    GREAT_BRITAIN("Great Britain"),
    USA("United States"),
    BELGIUM("Belgium"),
    NORWAY("Norway"),
    FINLAND("Finland"),
    HUNGARY("Hungary"),
    GREECE("Greece"),
    ARGENTINA("Argentina"),
    VIETNAM("Vietnam"),
    BALI_INDONESIA("Bali (Indonesia)"),
    SWITZERLAND("Switzerland"),
    PORTUGAL("Portugal");

    private final String countryName;



}

