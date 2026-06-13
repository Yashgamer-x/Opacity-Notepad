package org.yashgamerx.notepad.handler;

/**
 * Generates monotonically increasing tab numbers.
 *
 * <p>Instantiated once and injected wherever tab numbering is needed,
 * making it testable and avoiding global mutable static state.</p>
 */
public class TabNumberHandler {

    private int tabNumber = 1;

    public int postIncrement() {
        return tabNumber++;
    }

    public int current() {
        return tabNumber;
    }
}
