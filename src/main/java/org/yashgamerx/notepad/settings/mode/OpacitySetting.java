package org.yashgamerx.notepad.settings.mode;

import org.yashgamerx.notepad.settings.Settings;

public final class OpacitySetting extends Settings {
    public static void set(Number value){
        set("opacity", String.valueOf(value.intValue()));
    }
}
