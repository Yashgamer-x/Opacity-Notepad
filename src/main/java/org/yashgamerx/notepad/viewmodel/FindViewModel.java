package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FindViewModel {

    BooleanProperty isFindVisible = new SimpleBooleanProperty(false);

    public FindViewModel() {

    }

    public void toggleFindVisibility() {
        isFindVisible.set(!isFindVisible.get());
    }

    public void closeFind() {
        isFindVisible.set(false);
    }

    public BooleanProperty isFindVisibleProperty() {
        return isFindVisible;
    }
}
