package org.yashgamerx.notepad.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yashgamerx.notepad.service.FileService;
import org.yashgamerx.notepad.service.NotepadFileService;
import org.yashgamerx.notepad.service.PropertiesSettingsService;
import org.yashgamerx.notepad.service.SettingsService;
import org.yashgamerx.notepad.settings.OsDependentPathResolver;
import org.yashgamerx.notepad.settings.SettingsPathResolver;

@Configuration
public class Configurations {

    @Bean
    public FileService fileService() {
        return new NotepadFileService();
    }

    @Bean
    public SettingsPathResolver settingsPathResolver(){
        return new OsDependentPathResolver();
    }

    @Bean
    public SettingsService settingsService(SettingsPathResolver pathResolver) {
        return new PropertiesSettingsService(pathResolver);
    }

}
