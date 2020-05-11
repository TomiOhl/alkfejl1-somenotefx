package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.model.Note;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
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

        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");   // convert soft-breaks to hard breaks
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        com.vladsch.flexmark.util.ast.Node document = parser.parse("This is *Sparta*"); // TODO: beolvasott Note legyen a String
        String html = renderer.render(document);

        WebEngine webEngine = preview.getEngine();
        webEngine.loadContent(html, "text/html");
    }

}
