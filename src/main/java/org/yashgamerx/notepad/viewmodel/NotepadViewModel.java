package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.*;
import javafx.scene.text.Font;
import org.yashgamerx.notepad.service.SettingsService;

public class NotepadViewModel {
    private static final double DEFAULT_OPACITY = 100.0;
    private static final double DEFAULT_FONT_SIZE = 12.0;

    private final SettingsService settingsService;

    // Properties
    private final DoubleProperty opacity = new SimpleDoubleProperty(DEFAULT_OPACITY);
    private final BooleanProperty wordWrap = new SimpleBooleanProperty(false);
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font(DEFAULT_FONT_SIZE));

    // Constructor
    public NotepadViewModel(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /// Loads the preferences and sets them into the properties.
    public void loadSettings() {
        //Loads the preferences for Notepad like opacity, wordwrap and font-size
        double savedOpacity = Double.parseDouble(settingsService.get("opacity", "100"));
        boolean savedWordWrap = Boolean.parseBoolean(settingsService.get("wordwrap", "false"));
        double savedFontSize = Double.parseDouble(settingsService.get("font.size", "12"));

        //Sets the properties to the loaded values
        opacity.set(savedOpacity);
        wordWrap.set(savedWordWrap);
        font.set(new Font(savedFontSize));
    }

    /// Persists the current opacity value.
    ///
    /// The value ranges from `0` to `100`.
    public void saveOpacity() {
        settingsService.set("opacity", String.valueOf(opacity.get()));
        settingsService.save();
    }

    /// Persists the current wordwrap value.
    ///
    /// The value is either `true` or `false`.
    public void saveWordWrapSetting() {
        settingsService.set("wordwrap", String.valueOf(wordWrap.get()));
        settingsService.save();
    }

    /// Increases the font-size by 1 and invokes [NotepadViewModel#saveFontSize()]
    public void increaseFontSize() {
        double newSize = font.get().getSize() + 1;
        font.set(new Font(newSize));
        saveFontSize();
    }

    /// Decreases the font-size by 1 and invokes [NotepadViewModel#saveFontSize()]
    public void decreaseFontSize() {
        double currentSize = font.get().getSize();

        if (currentSize <= 1) {
            return;
        }

        double newSize = currentSize - 1;
        font.set(new Font(newSize));
        saveFontSize();
    }

    /// Persists the current font-size value
    private void saveFontSize() {
        settingsService.set("font.size", String.valueOf(font.get().getSize()));
        settingsService.save();
    }

    // Getters and Setters

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
