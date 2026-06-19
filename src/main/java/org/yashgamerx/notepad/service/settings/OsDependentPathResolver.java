package org.yashgamerx.notepad.service.settings;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public final class OsDependentPathResolver implements SettingsPathResolver {

    private final String osName;
    private final String userHome;
    private final String appData;

    public OsDependentPathResolver() {
        this.osName = System.getProperty("os.name").toLowerCase();
        this.userHome = System.getProperty("user.home");
        this.appData = System.getenv("APPDATA");
    }

    // Overloaded constructor for unit testing environments
    public OsDependentPathResolver(String osName, String userHome, String appData) {
        this.osName = osName.toLowerCase();
        this.userHome = userHome;
        this.appData = appData;
    }

    @Override
    public File resolve() {
        String baseDir;

        if (osName.contains("win")) {
            baseDir = appData != null ? appData : userHome;
        } else if (osName.contains("mac")) {
            baseDir = userHome + "/Library/Application Support";
        } else {
            baseDir = userHome + "/.config";
        }

        String folderName = osName.contains("win") || osName.contains("mac") ? "NotepadApp" : "notepad-app";

        File dir = new File(baseDir, folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, "notepad-settings.properties");
    }
}
