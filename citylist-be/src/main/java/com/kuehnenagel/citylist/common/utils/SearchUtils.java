package com.kuehnenagel.citylist.common.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class for <code>JpaSpecificationExecutor</code> implementations.
 * It provides handy methods to create 'search request' specifications (in the sense of Domain Driven Design).
 */
public final class SearchUtils {

    private static final int SORT_BY_FIRST_INDEX_WITHOUT_SORT_DIRECTION = 1;
    private static final String DESC = "-";
    private static final int SINGLE_SPECIFICATION_ARRAY_SIZE = 1;
    private static final int FIRST_SPECIFICATION = 0;
    private static final int SUBSEQUENT_SPECIFICATION = 1;
    private static final int ROOT = 0;

    private SearchUtils() { }


    /**
     * Creates <code>Specification</code> from both 'AND'(solid)statements.
     *
     * @param andConditions - a list of 'AND' statements
     * @param <T>           - type of the specification
     *
     * @return a united specification
     */
    public static <T> Specification<T> createSpecification(List<Specification<T>> andConditions) {

        List<Specification<T>> nonNullConditions = andConditions.stream()
                .filter(Objects::nonNull)
                .toList();
        Specification<T> specification = null;

        if (!nonNullConditions.isEmpty()) {
            specification = addAndSpecification(nonNullConditions);
        }
        return specification;
    }

    /**
     * Creates <code>Sort</code> option for <code>Specification</code> based queries.
     *
     * @param sortBy       shortcut of a parameter to sort by
     * @param defaultValue a parameter that will be used if a passed shortcut doesn't have a value
     *
     * @return a sort option
     */
    public static Sort getSortSettings(String sortBy, String defaultValue) {
        if (sortBy == null) {
            sortBy = defaultValue;
        }
        boolean isDesc = sortBy.startsWith(DESC);

        String sortByFieldToCheck = isDesc ? sortBy.substring(SORT_BY_FIRST_INDEX_WITHOUT_SORT_DIRECTION) : sortBy;

        Sort.Direction sortDirection = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortByFieldToCheck);
    }

    private static <T> Specification<T> addAndSpecification(List<Specification<T>> addConditions) {
        Specification<T> specification;
        specification = addConditions.get(FIRST_SPECIFICATION);

        if (addConditions.size() > SINGLE_SPECIFICATION_ARRAY_SIZE) {
            for (int i = SUBSEQUENT_SPECIFICATION; i < addConditions.size(); i++) {
                specification = specification.and(addConditions.get(i));
            }
        }
        return specification;
    }

    /**
     * Creates <code>Specification</code> statement.
     *
     * @param parameterGetter a supplier that holds the parameter for SQL search condition
     * @param entityParams    an array of an entity parameters
     * @param <T>             the type of the {@link Root} the resulting {@literal Specification} operates on
     * @param <E>             the type of results supplied by 'parameterGetter'
     *
     * @return a specification or null if a condition parameter wasn't passed
     */
    public static <T, E> Specification<T> createEqualsStatement(
            Supplier<E> parameterGetter,
            String... entityParams) {

        Specification<T> specification = null;

        if (parameterGetter.get() != null) {
            specification = (root, query, builder)
                    -> builder.equal(getExpression(root, entityParams), parameterGetter.get());
        }

        return specification;
    }

    private static <T, E> Expression<E> getExpression(Root<T> queryRoot, String[] params) {
        Path<E> expression = queryRoot.get(params[ROOT]);
        if (params.length > 1) {
            for (int i = 1; i < params.length; i++) {
                expression = expression.get(params[i]);
            }
        }
        return expression;
    }

}
