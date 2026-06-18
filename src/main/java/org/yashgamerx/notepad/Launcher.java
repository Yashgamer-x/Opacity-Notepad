package org.yashgamerx.notepad;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.yashgamerx.notepad")
public class Launcher {
    public static void main(String[] args) {
        // Initialize the IoC container
        ApplicationContext context = new AnnotationConfigApplicationContext(Launcher.class);

        Application.launch(NotepadApplication.class, args);
    }
}
