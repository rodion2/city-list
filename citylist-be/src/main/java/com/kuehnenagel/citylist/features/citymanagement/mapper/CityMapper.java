package com.kuehnenagel.citylist.features.citymanagement.mapper;

import com.kuehnenagel.citylist.features.citymanagement.dto.CityDto;
import com.kuehnenagel.citylist.features.citymanagement.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDto map(City city);

}
