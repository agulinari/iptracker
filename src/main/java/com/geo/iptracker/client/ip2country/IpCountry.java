package com.geo.iptracker.client.ip2country;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpCountry {

    private String countryCode;
    private String countryCode3;
    private String countryName;
    private String countryEmoji;

}
