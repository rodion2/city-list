package com.kuehnenagel.citylist.common.persistence;

import java.io.Serializable;

/**
 * Represents an ability to be identified by ID for JPA managed entities.
 *
 * @param <T> id format, string or numeric based.
 */

public interface Identifiable<T extends Serializable> extends Serializable {

    T getId();
}