package org.yashgamerx.notepad.service.find;

import java.util.List;

/**
 * SRP / DIP: Abstracts the strategy for finding occurrences of a query
 * within a block of text.  The view and view-model depend on this interface,
 * not on a concrete regex or string-search implementation.
 */
public interface TextFinder {

    /**
     * Returns every non-overlapping occurrence of {@code query} in {@code text},
     * ordered by ascending start position.
     *
     * @param text    the content to search
     * @param query   the search term (must not be blank)
     * @param options controls case-sensitivity and whole-word matching
     * @return an immutable list of {@link FindResult}s; empty when nothing matches
     */
    List<FindResult> findAll(String text, String query, FindOptions options);
}
