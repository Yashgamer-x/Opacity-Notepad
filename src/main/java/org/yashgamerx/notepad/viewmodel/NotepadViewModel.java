package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.*;
import javafx.scene.text.Font;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.yashgamerx.notepad.service.settings.SettingsService;

import java.util.logging.Level;

/**
 * ViewModel for the main notepad window.
 *
 * <h3>SOLID notes</h3>
 * <ul>
 *   <li><b>SRP</b>: owns global UI state (opacity, word-wrap, font). Auto-saves
 *       are registered here via property listeners so no caller needs to remember
 *       to call a save method.</li>
 *   <li><b>DIP</b>: depends on {@link SettingsService} (interface).</li>
 *   <li><b>Bug fix</b>: parse errors fall back to defaults instead of throwing
 *       {@link NumberFormatException}.</li>
 * </ul>
 */
@Log
@Component
public class NotepadViewModel {

    /** Single source of truth for default font size — shared with {@code NotepadTabView}. */
    public static final double DEFAULT_FONT_SIZE = 12.0;
    private static final double DEFAULT_OPACITY  = 100.0;
    private static final double MIN_FONT_SIZE    = 1.0;

    private static final String KEY_OPACITY   = "opacity";
    private static final String KEY_WORD_WRAP = "wordwrap";
    private static final String KEY_FONT_SIZE = "font.size";

    private final SettingsService settingsService;

    private final DoubleProperty opacity  = new SimpleDoubleProperty(DEFAULT_OPACITY);
    private final BooleanProperty wordWrap = new SimpleBooleanProperty(false);
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font(DEFAULT_FONT_SIZE));

    public NotepadViewModel(SettingsService settingsService) {
        this.settingsService = settingsService;
        registerAutoSaveListeners();
    }

    /**
     * Loads persisted preferences and applies them to the properties.
     * Individual parse errors are caught so a single corrupt entry does not
     * prevent the other settings from loading.
     */
    public void loadSettings() {
        opacity.set(parseDouble (settingsService.get(KEY_OPACITY,   null), DEFAULT_OPACITY));
        wordWrap.set(parseBoolean(settingsService.get(KEY_WORD_WRAP, null), false));
        font.set(new Font(parseDouble(settingsService.get(KEY_FONT_SIZE, null), DEFAULT_FONT_SIZE)));
    }

    public void increaseFontSize() {
        font.set(new Font(font.get().getSize() + 1));
    }

    public void decreaseFontSize() {
        double current = font.get().getSize();
        if (current > MIN_FONT_SIZE) {
            font.set(new Font(current - 1));
        }
    }

    // --- Property accessors ---

    public DoubleProperty opacityProperty() { return opacity; }
    public BooleanProperty wordWrapProperty() { return wordWrap; }
    public ObjectProperty<Font> fontProperty() { return font; }

    public static double getDefaultFontSize() { return DEFAULT_FONT_SIZE; }

    // --- Private helpers ---

    /**
     * Registers listeners that persist each setting automatically when the
     * property value changes.  This removes the burden from callers and ensures
     * settings are never accidentally lost.
     */
    private void registerAutoSaveListeners() {
        opacity.addListener((_, _, nw) -> save(KEY_OPACITY, nw.toString()));
        wordWrap.addListener((_, _, nw) -> save(KEY_WORD_WRAP, nw.toString()));
        font.addListener((_, _, nw) -> save(KEY_FONT_SIZE, String.valueOf(nw.getSize())));
    }

    private void save(String key, String value) {
        settingsService.set(key, value);
        settingsService.save();
    }

    private double parseDouble(String raw, double fallback) {
        if (raw == null || raw.isBlank()) return fallback;
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "Invalid numeric setting \"{0}\", using default {1}",
                    new Object[]{raw, fallback});
            return fallback;
        }
    }

    private boolean parseBoolean(String raw, boolean fallback) {
        return raw == null ? fallback : Boolean.parseBoolean(raw);
    }
}
