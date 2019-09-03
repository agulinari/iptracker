package com.geo.iptracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpTrackerResponse {

    private String ip;
    private LocalDateTime date;
    private String countryName;
    private String isoCode;
    private List<LocalDateTime> localTimes;
    private Distance distanceToBsAs;
    private List<Currency> currencies;
    private List<Language> languages;
}
