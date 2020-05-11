package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.utils.ConverterUtils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.web.*;
import javafx.stage.Stage;

import java.net.URL;
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
                return ConverterUtils.convertToHtml();
            }
        };
        // siker esetén jelenítsük meg a tartalmat
        loadPreview.setOnSucceeded(event -> {
            // kérjük el a Task eredményeként kapot Stringet
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
