package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.App;
import com.tomiohl.somenotefx.model.Note;
import com.tomiohl.somenotefx.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private Note currentNote;

    @FXML
    private TextArea noteTextArea;

    @FXML
    public void addNote(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/tomiohl/somenotefx/view/save_dialog.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Mentés mint");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openRecents(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/tomiohl/somenotefx/view/recents_dialog.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Legutóbbiak");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openFile(ActionEvent actionEvent) {
        // nyitunk egy choosert
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Válasszon egy szövegfájlt");
        // megadunk filtereket a kiterjesztésre
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT-fájlok", "*.txt");
        chooser.getExtensionFilters().add(txtFilter);
        FileChooser.ExtensionFilter mdFilter = new FileChooser.ExtensionFilter("Markdown-fájlok", "*.md");
        chooser.getExtensionFilters().add(mdFilter);
        FileChooser.ExtensionFilter noFilter = new FileChooser.ExtensionFilter("Minden fájl", "*");
        chooser.getExtensionFilters().add(noFilter);
        // választó ablak
        File selectedFile = chooser.showOpenDialog(App.getMainStage());
        // beolvassuk a fájlt
        if (selectedFile != null) {
            try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                    line = reader.readLine();
                }
                noteTextArea.setText(builder.toString());
                // létrehozunk egy Note objektumot, amit majd a mentésnél használhatunk
                Path path = Paths.get(selectedFile.getAbsolutePath());
                currentNote = new Note(selectedFile.getName(), path.getParent().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //noteTextArea.setText(selectedFile.getAbsolutePath());
        } else {
            Utils.showWarning("Nem választott fájlt");
        }
    }

    public MainWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
