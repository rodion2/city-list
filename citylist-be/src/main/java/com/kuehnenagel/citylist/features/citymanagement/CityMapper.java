package com.kuehnenagel.citylist.features.citymanagement;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDto map(City city);

}
