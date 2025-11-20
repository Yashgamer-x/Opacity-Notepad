package org.yashgamerx.notepad.model;

import lombok.Getter;
import lombok.Setter;
import org.yashgamerx.notepad.controller.NotepadTabController;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotepadModel {
    List<NotepadTabController> notepadTabControllerList;

    public NotepadModel() {
        notepadTabControllerList = new ArrayList<>();
    }
}
