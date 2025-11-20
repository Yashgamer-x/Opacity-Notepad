package org.yashgamerx.notepad.handler;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordWrapHandler {
    @Getter @Setter
    private static BooleanProperty wordWrapBooleanProperty = new SimpleBooleanProperty(false);
}
