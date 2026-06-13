package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.*;
import javafx.scene.text.Font;
import org.yashgamerx.notepad.service.SettingsService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ViewModel for the main notepad window.
 *
 * <h3>Changes from the original</h3>
 * <ul>
 *   <li>Opacity / word-wrap / font-size are saved automatically via property
 *       listeners registered in the constructor, removing the need for callers
 *       to call {@code saveOpacity()} / {@code saveWordWrapSetting()} manually.</li>
 *   <li>{@link #loadSettings()} is now safe against a corrupt or missing
 *       properties file: parse errors fall back to defaults and log a warning
 *       instead of throwing {@link NumberFormatException}.</li>
 *   <li>{@code DEFAULT_FONT_SIZE} is the single source of truth shared with
 *       {@code NotepadTabView} via {@link #getDefaultFontSize()}.</li>
 * </ul>
 */
public class NotepadViewModel {

    private static final Logger log = Logger.getLogger(NotepadViewModel.class.getName());

    // Single source of truth for the default font size.
    // NotepadTabView reads this constant instead of duplicating the value.
    public static final double DEFAULT_FONT_SIZE = 12.0;
    private static final double DEFAULT_OPACITY = 100.0;
    private static final double MIN_FONT_SIZE = 1.0;

    private static final String KEY_OPACITY = "opacity";
    private static final String KEY_WORD_WRAP = "wordwrap";
    private static final String KEY_FONT_SIZE = "font.size";

    private final SettingsService settingsService;

    // Properties
    private final DoubleProperty opacity = new SimpleDoubleProperty(DEFAULT_OPACITY);
    private final BooleanProperty wordWrap = new SimpleBooleanProperty(false);
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(new Font(DEFAULT_FONT_SIZE));

    public NotepadViewModel(SettingsService settingsService) {
        this.settingsService = settingsService;

        // Auto-persist whenever a property changes, so callers never have to
        // remember to call a separate save method.
        opacity.addListener((_, _, _) -> saveDouble(KEY_OPACITY, opacity.get()));
        wordWrap.addListener((_, _, _) -> saveBoolean(KEY_WORD_WRAP, wordWrap.get()));
        font.addListener((_, _, _) -> saveDouble(KEY_FONT_SIZE, font.get().getSize()));
    }

    /**
     * Loads persisted preferences and applies them to the properties.
     *
     * <p>Parsing errors are caught individually so a single corrupt entry
     * does not prevent the other settings from loading.</p>
     */
    public void loadSettings() {
        opacity.set(parseDouble(settingsService.get(KEY_OPACITY, null), DEFAULT_OPACITY));
        wordWrap.set(parseBoolean(settingsService.get(KEY_WORD_WRAP, null), false));
        font.set(new Font(parseDouble(settingsService.get(KEY_FONT_SIZE, null), DEFAULT_FONT_SIZE)));
    }

    public void increaseFontSize() {
        font.set(new Font(font.get().getSize() + 1));
    }

    public void decreaseFontSize() {
        double currentSize = font.get().getSize();
        if (currentSize > MIN_FONT_SIZE) {
            font.set(new Font(currentSize - 1));
        }
    }

    // --- Property accessors ---

    public DoubleProperty opacityProperty() {
        return opacity;
    }

    public BooleanProperty wordWrapProperty() {
        return wordWrap;
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public static double getDefaultFontSize() {
        return DEFAULT_FONT_SIZE;
    }

    // --- Private helpers ---

    private void saveDouble(String key, double value) {
        settingsService.set(key, String.valueOf(value));
        settingsService.save();
    }

    private void saveBoolean(String key, boolean value) {
        settingsService.set(key, String.valueOf(value));
        settingsService.save();
    }

    /**
     * Parses a double from {@code raw}, returning {@code fallback} on any error.
     */
    private double parseDouble(String raw, double fallback) {
        if (raw == null || raw.isBlank()) return fallback;
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "Invalid numeric setting value \"{0}\", using default {1}",
                    new Object[]{raw, fallback});
            return fallback;
        }
    }

    /**
     * Parses a boolean from {@code raw}, returning {@code fallback} when {@code raw} is null.
     */
    private boolean parseBoolean(String raw, boolean fallback) {
        if (raw == null) return fallback;
        return Boolean.parseBoolean(raw);
    }
}
