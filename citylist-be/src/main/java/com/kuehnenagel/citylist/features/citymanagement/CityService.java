package com.kuehnenagel.citylist.features.citymanagement;

import java.util.ArrayList;
import java.util.List;

import com.kuehnenagel.citylist.common.error.ServiceError;
import com.kuehnenagel.citylist.common.error.ServiceException;
import com.kuehnenagel.citylist.common.utils.SearchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

    private static final String DEFAULT_SORT_BY_FIELD = "name";

    private final CityRepository repository;
    private final CityMapper mapper;
    private final CityValidator cityValidator;

    public Page<CityDto> findCities(CitySearchDto searchDto) {
        Specification<City> specification = getSpecification(searchDto);
        Sort sortSettings = SearchUtils.getSortSettings(searchDto.getSortBy(), DEFAULT_SORT_BY_FIELD);
        PageRequest pageRequest = PageRequest.of(searchDto.getPageNumber(), searchDto.getPageSize(), sortSettings);
        return repository.findAll(specification, pageRequest).map(mapper::map);
    }

    private Specification<City> getSpecification(CitySearchDto searchDto) {
        List<Specification<City>> andConditions = new ArrayList<>();
        andConditions.add(SearchUtils.createEqualsStatement(searchDto::getCityName, "name"));
        return SearchUtils.createSpecification(andConditions);
    }

    public CityDto updateCity(CityDto cityDto) {
        cityValidator.validate(cityDto);

        City city = repository.findById(cityDto.getId())
                .orElseThrow(() -> ServiceException.builder(ServiceError.CITY_NOT_FOUND_EXCEPTION)
                        .messageParameters(cityDto.getId())
                        .build());

        city.setName(cityDto.getName());
        city.setPhotoLink(cityDto.getPhotoLink());
        return mapper.map(repository.save(city));
    }

}
