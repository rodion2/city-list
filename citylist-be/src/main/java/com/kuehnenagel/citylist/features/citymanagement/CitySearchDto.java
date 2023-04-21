package com.kuehnenagel.citylist.features.citymanagement;

import java.util.List;

import com.kuehnenagel.citylist.common.validation.SearchParamsValidator;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CitySearchDto {

    private static final List<String> ALLOWED_SORT_BY_FIELDS = List.of("name");

    private String cityName;

    private Integer pageNumber;

    private Integer pageSize;

    private String sortBy;

    public static CitySearchDtoBuilder builder() {
        return new CustomBuilder();
    }

    private static class CustomBuilder extends CitySearchDtoBuilder {
        public CitySearchDto build() {
            SearchParamsValidator.validate(ALLOWED_SORT_BY_FIELDS, super.sortBy, super.pageSize, super.pageNumber);
            return super.build();
        }
    }

}
