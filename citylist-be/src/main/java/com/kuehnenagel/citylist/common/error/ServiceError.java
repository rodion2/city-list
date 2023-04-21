package com.kuehnenagel.citylist.common.error;

import java.text.MessageFormat;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ServiceError {

    // 4xx
    CITY_NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, "Requested city [{0}] was not found in the system."),
    INVALID_CITY_PHOTO_LINK(HttpStatus.BAD_REQUEST, "[{0}] city photo link [{1}] - is invalid "),
    INVALID_CITY_NAME(HttpStatus.BAD_REQUEST, "City name can't be empty."),
    INVALID_URL(HttpStatus.BAD_REQUEST, "Invalid URL [{0}]"),
    NO_PERMISSIONS(HttpStatus.FORBIDDEN, "A user is not allowed to perform an action."),
    INVALID_SEARCH_REQUEST(HttpStatus.BAD_REQUEST, "Validation error: [{0}]."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User [{0}] not found");

    // 5xx

    private HttpStatus status;
    private String message;

    public String getMessage(Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return message;
        }

        MessageFormat formatter = new MessageFormat(message, Locale.ROOT);
        return formatter.format(parameters);
    }
}
