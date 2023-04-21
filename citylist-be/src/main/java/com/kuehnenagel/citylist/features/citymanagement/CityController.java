package com.kuehnenagel.citylist.features.citymanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Slf4j
public class CityController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_OBJECTS_ON_PAGE_COUNT = "20";
    public static final String DEFAULT_SORT_BY = "name";

    private final CityService cityService;

    @GetMapping
    public Page<CityDto> findCities(
            @RequestParam(name = "name", required = false) String cityName,
            @RequestParam(name = "sortBy", required = false, defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = DEFAULT_OBJECTS_ON_PAGE_COUNT) int pageSize) {

        CitySearchDto searchDto = CitySearchDto.builder()
                .cityName(cityName)
                .sortBy(sortBy)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        return cityService.findCities(searchDto);
    }

    @PutMapping
    public CityDto updateCity(@RequestBody CityDto cityDto) {
        return cityService.updateCity(cityDto);
    }

}
