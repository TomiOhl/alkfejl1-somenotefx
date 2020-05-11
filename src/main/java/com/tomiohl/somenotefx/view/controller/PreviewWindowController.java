package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.controller.NoteController;
import com.tomiohl.somenotefx.model.Note;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.web.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class PreviewWindowController implements Initializable {

    @FXML
    private WebView preview;

    @FXML
    public void closeDialog(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public PreviewWindowController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = preview.getEngine();
        Task<String> loadPreview = loadPreview(webEngine);
        new Thread(loadPreview).start();    // külön szálon végezzük
    }

    // maga a content betöltését végző task
    private Task<String> loadPreview(WebEngine webEngine) {
        Task<String> loadPreview = new Task<>() {
            @Override
            protected String call() {
                MutableDataSet options = new MutableDataSet();
                options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");   // convert soft-breaks to hard breaks
                Parser parser = Parser.builder(options).build();
                HtmlRenderer renderer = HtmlRenderer.builder(options).build();
                Note currentNote = NoteController.getInstance().getCurrentNote();
                File selectedFile = new File(Path.of(currentNote.getFilePath(), currentNote.getFilename()).toString());
                com.vladsch.flexmark.util.ast.Node document = parser.parse(NoteController.getInstance().open(selectedFile));
                return renderer.render(document);
            }
        };

        // siker esetén jelenítsük meg a tartalmat
        loadPreview.setOnSucceeded(event -> {
            // Get the string returned by the task body.
            final String document = (String) event.getSource().getValue();
            if (document != null) {
                webEngine.loadContent(document, "text/html");
            }
        });

        // hiba esetén írjuk ki a hibát
        loadPreview.setOnFailed(event -> {
            Exception e = (Exception) event.getSource().getException();
            // ha nullpointer volt, akkor valószínűleg simán csak üres volt a fájlunk
            if (e instanceof NullPointerException)
                webEngine.loadContent("", "text/plain");
            else
                webEngine.loadContent(e.toString(), "text/plain");
        });

        return loadPreview;
    }


}
