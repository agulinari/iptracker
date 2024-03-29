package com.geo.iptracker.client.restcountries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    private String iso639_1;
    private String name;

}
