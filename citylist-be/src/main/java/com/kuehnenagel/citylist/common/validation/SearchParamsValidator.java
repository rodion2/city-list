package com.kuehnenagel.citylist.common.validation;

import java.util.List;

import com.kuehnenagel.citylist.common.error.ServiceError;
import com.kuehnenagel.citylist.common.error.ServiceException;

/**
 * Validates 'GET' endpoints' parameters that perform a search.
 */

public class SearchParamsValidator {

    private static final int MINIMAL_PAGE_SIZE = 1;
    private static final int MINIMAL_PAGE = 0;
    private static final int SORT_BY_FIRST_INDEX_WITHOUT_SORT_DIRECTION = 1;

    private SearchParamsValidator() { }

    public static void validate(List<String> allowedSortByFields, String sortBy, Integer size, Integer page) {
        String sortByFieldToCheck = sortBy.startsWith("-")
                ? sortBy.substring(SORT_BY_FIRST_INDEX_WITHOUT_SORT_DIRECTION) : sortBy;

        if (!allowedSortByFields.contains(sortByFieldToCheck)) {
            throw ServiceException.builder(ServiceError.INVALID_SEARCH_REQUEST)
                    .messageParameters(String.format("Wrong 'sortBy' field name - [%s].", sortBy))
                    .build();
        }

        if (size != null && size < MINIMAL_PAGE_SIZE) {
            throw ServiceException.builder(ServiceError.INVALID_SEARCH_REQUEST)
                    .messageParameters("Page size value can`t be less than 1.")
                    .build();
        }

        if (page != null && page < MINIMAL_PAGE) {
            throw ServiceException.builder(ServiceError.INVALID_SEARCH_REQUEST)
                    .messageParameters("Page value can`t be negative")
                    .build();
        }
    }

}

