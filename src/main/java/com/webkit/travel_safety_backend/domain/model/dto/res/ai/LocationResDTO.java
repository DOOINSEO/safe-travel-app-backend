package com.webkit.travel_safety_backend.domain.model.dto.res.ai;

import com.webkit.travel_safety_backend.domain.model.entity.Locations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResDTO {

    private Long id;
    private String countryCode;
    private String countryName;
    private String regionCode;
    private String regionName;
    private Short safetyStage;

    public static LocationResDTO from(Locations locations) {
        if (locations == null) return null;
        return LocationResDTO.builder()
                .id(locations.getId())
                .countryCode(locations.getCountryCode())
                .countryName(locations.getCountryName())
                .regionCode(locations.getRegionCode())
                .regionName(locations.getRegionName())
                .safetyStage(locations.getSafetyStage())
                .build();
    }

}
