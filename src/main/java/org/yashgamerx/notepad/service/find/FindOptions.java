package org.yashgamerx.notepad.service.find;

/**
 * Immutable options controlling how a find operation is performed.
 *
 * @param caseSensitive whether the search is case-sensitive
 * @param wholeWord     whether the search matches whole words only
 */
public record FindOptions(boolean caseSensitive, boolean wholeWord) {

    /** Convenience factory: plain case-insensitive, partial-word search. */
    public static FindOptions defaults() {
        return new FindOptions(false, false);
    }
}
