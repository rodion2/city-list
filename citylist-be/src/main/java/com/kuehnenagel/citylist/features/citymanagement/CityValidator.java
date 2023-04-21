package com.kuehnenagel.citylist.features.citymanagement;

import com.kuehnenagel.citylist.common.error.ServiceError;
import com.kuehnenagel.citylist.common.error.ServiceException;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.UrlValidator;

@Service
public class CityValidator {

    private final UrlValidator urlValidator = new UrlValidator();

    public void validate(CityDto dto) {
        String cityName = dto.getName();
        if (cityName == null || cityName.isBlank()) {
            throw ServiceException.builder(ServiceError.INVALID_CITY_NAME).messageParameters(cityName).build();
        }

        String photoLink = dto.getPhotoLink();
        if (!urlValidator.isValid(photoLink)) {
            throw ServiceException.builder(ServiceError.INVALID_CITY_PHOTO_LINK).messageParameters(
                    cityName,
                    photoLink
            ).build();
        }
    }

}
