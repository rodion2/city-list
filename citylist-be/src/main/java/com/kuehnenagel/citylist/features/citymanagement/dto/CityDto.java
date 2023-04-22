package com.kuehnenagel.citylist.features.citymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {

    private Long id;
    private String name;
    private String photoLink;

}
