package org.yashgamerx.notepad.handler;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StageHandler{
    @Getter @Setter
    private static Stage stage;
}
