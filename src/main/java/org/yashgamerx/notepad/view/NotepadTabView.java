package org.yashgamerx.notepad.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;
import java.io.IOException;

@Log
@Getter
public class NotepadTabView extends Tab {

    // Constants
    private static final double BASE_FONT_SIZE = 12.0;

    // FXML Elements
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

    // View Model
    private NotepadTabViewModel viewModel;

    // Constructor
    public NotepadTabView() {
        loadFXML();
    }

    /// Loads the notepad-tab-template.fxml, sets this object as the root and controller.
    /// Finally loads the FXML.
    private void loadFXML(){
        var loader = new FXMLLoader(
                getClass().getResource("/org/yashgamerx/notepad/view/notepad-tab-template.fxml")
        );

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            log.severe("Failed to load notepad-tab-template.fxml");
        }
    }

    public void bind(
            NotepadTabViewModel viewModel,
            BooleanProperty wordWrapProperty,
            ObjectProperty<Font> fontProperty
    ) {
        this.viewModel = viewModel;

        // Binds TextArea's properties like textProperty, wrapTextProperty, and fontProperty
        textArea.textProperty().bindBidirectional(viewModel.contentProperty());
        textArea.wrapTextProperty().bind(wordWrapProperty);
        textArea.fontProperty().bind(fontProperty);

        // Binds tab textProperty
        tab.textProperty().bind(viewModel.displayTitleBinding());

        // Binds numberOfLines textProperty
        numberOfLines.textProperty().bind(viewModel.lineCountBinding());

        // Binds characters textProperty
        characters.textProperty().bind(viewModel.characterCountBinding());

        // Binds zoomPercentage textProperty
        var percentBinding = Bindings.createDoubleBinding(
                () -> (fontProperty.get().getSize() / BASE_FONT_SIZE) * 100.0,
                fontProperty
        );
        zoomPercentage.textProperty().bind(percentBinding.asString("%.0f%%"));
    }

    /// Unbinds [NotepadTabView#textArea], [NotepadTabView#tab], [NotepadTabView#numberOfLines],
    ///  [NotepadTabView#characters], and [NotepadTabView#zoomPercentage]
    public void unbind() {
        //Text Area unbinding
        textArea.textProperty().unbindBidirectional(viewModel.contentProperty());
        textArea.wrapTextProperty().unbind();
        textArea.fontProperty().unbind();

        //Tab unbinding
        tab.textProperty().unbind();

        //Number of Lines unbinding
        numberOfLines.textProperty().unbind();

        //Characters unbinding
        characters.textProperty().unbind();

        //Zoom Percentage unbind
        zoomPercentage.textProperty().unbind();
    }
}