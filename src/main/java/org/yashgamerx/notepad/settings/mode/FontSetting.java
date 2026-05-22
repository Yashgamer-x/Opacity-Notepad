package org.yashgamerx.notepad.settings.mode;

import org.yashgamerx.notepad.settings.Settings;

public final class FontSetting extends Settings {

    public static void set(double size){
        set("font.size", String.valueOf(size));
    }
}
