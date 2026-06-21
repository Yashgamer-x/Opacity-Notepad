package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.StringProperty;

public interface FindTextContext {
    StringProperty textAreaProperty();
    int getCaretPosition();
    void highlightAndScrollTo(int start, int end);
}
