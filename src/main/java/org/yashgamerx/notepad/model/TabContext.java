package org.yashgamerx.notepad.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.text.Font;
import java.nio.file.Path;

public record TabContext(
        Path filePath,
        BooleanProperty wordWrap,
        ObjectProperty<Font> font
) {}
