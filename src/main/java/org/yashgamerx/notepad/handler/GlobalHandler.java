package org.yashgamerx.notepad.handler;

import javafx.beans.property.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalHandler {
    @Getter @Setter
    private static Stage stage;

    @Getter
    private final static BooleanProperty wordWrapBooleanProperty = new SimpleBooleanProperty(false);

    @Getter
    private final static ObjectProperty<Font> currentFont = new SimpleObjectProperty<>(Font.getDefault());
}
