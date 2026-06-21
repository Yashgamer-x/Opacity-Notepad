package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindViewModel {

    BooleanProperty isFindVisible = new SimpleBooleanProperty(false);
    StringProperty text = new SimpleStringProperty("");
    StringProperty findText = new SimpleStringProperty("");

    public void toggleFindVisibility() {
        isFindVisible.set(!isFindVisible.get());
    }

    public void closeFind() {
        isFindVisible.set(false);
    }

    public int findNextWordIndex(int currentIndex) {
        var text = this.text.get();
        return text.indexOf(findText.get(), currentIndex);
    }

    public int findPreviousWordIndex(int currentIndex) {
        String textRaw = this.text.get();
        String queryRaw = this.findText.get();

        if (textRaw == null || queryRaw == null || queryRaw.isEmpty()) {
            return -1;
        }

        // To prevent matching the current word again, the maximum allowed starting
        // index for the previous match must be strictly before the current word's start.
        int searchStartIndex = currentIndex - queryRaw.length() - 1;

        // Boundary protection: if we're already at the very beginning of the document
        if (searchStartIndex < 0) {
            return -1;
        }

        return textRaw.lastIndexOf(queryRaw, searchStartIndex);
    }

    // --- Property accessors ---
    public BooleanProperty isFindVisibleProperty() {
        return isFindVisible;
    }
    public StringProperty textProperty() {
        return text;
    }
    public StringProperty findTextProperty() {
        return findText;
    }
}
