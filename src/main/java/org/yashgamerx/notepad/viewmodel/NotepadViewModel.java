package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.*;
import javafx.scene.text.Font;
import org.yashgamerx.notepad.service.SettingsService;

public class NotepadViewModel {
    private static final double DEFAULT_OPACITY = 100.0;
    private static final double DEFAULT_FONT_SIZE = 12.0;

    private final SettingsService settingsService;

    private final DoubleProperty opacity = new SimpleDoubleProperty(DEFAULT_OPACITY);
    private final BooleanProperty wordWrap = new SimpleBooleanProperty(false);
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font(DEFAULT_FONT_SIZE));

    public NotepadViewModel(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void loadSettings() {
        double savedOpacity = Double.parseDouble(settingsService.get("opacity", "100"));
        boolean savedWordWrap = Boolean.parseBoolean(settingsService.get("wordwrap", "false"));
        double savedFontSize = Double.parseDouble(settingsService.get("font.size", "12"));

        opacity.set(savedOpacity);
        wordWrap.set(savedWordWrap);
        font.set(new Font(savedFontSize));
    }

    public void saveOpacity() {
        settingsService.set("opacity", String.valueOf(opacity.get()));
        settingsService.save();
    }

    public void saveWordWrap() {
        settingsService.set("wordwrap", String.valueOf(wordWrap.get()));
        settingsService.save();
    }

    public void increaseFontSize() {
        double newSize = font.get().getSize() + 1;
        font.set(new Font(newSize));
        saveFontSize();
    }

    public void decreaseFontSize() {
        double currentSize = font.get().getSize();

        if (currentSize <= 1) {
            return;
        }

        double newSize = currentSize - 1;
        font.set(new Font(newSize));
        saveFontSize();
    }

    private void saveFontSize() {
        settingsService.set("font.size", String.valueOf(font.get().getSize()));
        settingsService.save();
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    public BooleanProperty wordWrapProperty() {
        return wordWrap;
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }
}
