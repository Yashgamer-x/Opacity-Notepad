package org.yashgamerx.notepad.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import lombok.Getter;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;

@Getter
public class NotepadTabController {

    private static final double BASE_FONT_SIZE = 12.0;

    @FXML
    private Tab tab;
    @FXML
    private TextArea textArea;
    @FXML
    private Label numberOfLines;
    @FXML
    private Label characters;
    @FXML
    private Label zoomPercentage;

    private NotepadTabViewModel viewModel;

    public void bind(
            NotepadTabViewModel viewModel,
            BooleanProperty wordWrapProperty,
            ObjectProperty<Font> fontProperty
    ) {
        this.viewModel = viewModel;

        textArea.textProperty().bindBidirectional(viewModel.contentProperty());
        textArea.wrapTextProperty().bind(wordWrapProperty);
        textArea.fontProperty().bind(fontProperty);

        tab.textProperty().bind(viewModel.displayTitleBinding());
        numberOfLines.textProperty().bind(viewModel.lineCountBinding());
        characters.textProperty().bind(viewModel.characterCountBinding());

        var percentBinding = Bindings.createDoubleBinding(
                () -> (fontProperty.get().getSize() / BASE_FONT_SIZE) * 100.0,
                fontProperty
        );

        zoomPercentage.textProperty().bind(percentBinding.asString("%.0f%%"));
    }
}