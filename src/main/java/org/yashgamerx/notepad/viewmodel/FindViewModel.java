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
