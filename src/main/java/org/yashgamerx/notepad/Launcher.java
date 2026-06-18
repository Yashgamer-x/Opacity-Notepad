package org.yashgamerx.notepad;

import javafx.application.Application;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.yashgamerx.notepad")
public class Launcher {
    public static void main(String[] args) {
        Application.launch(NotepadApplication.class, args);
    }
}
