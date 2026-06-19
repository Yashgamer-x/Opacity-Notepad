package org.yashgamerx.notepad.service.find;

/**
 * Immutable value object representing a single match in the text.
 *
 * @param start inclusive start index of the match
 * @param end   exclusive end index of the match
 */
public record FindResult(int start, int end) {

    public FindResult {
        if (start < 0) throw new IllegalArgumentException("start must be >= 0");
        if (end < start) throw new IllegalArgumentException("end must be >= start");
    }
}
