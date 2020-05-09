package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.App;
import com.tomiohl.somenotefx.controller.NoteController;
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

import java.io.File;
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
    public void saveNote() {
        if (getCurrentNote() != null) {
            currentNote.setSaveDate(System.currentTimeMillis());
            if (NoteController.getInstance().save(getCurrentNote())) {
                // sikerült menteni a fájlt
                System.out.println(currentNote);
            } else {
                Utils.showWarning("A mentés nem sikerült");
            }
        } else {
            // TODO
        }
    }

    @FXML
    public void saveNoteAs(){
        // mentés másként dialógus
        Parent root;
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
            // TODO: visszakérni a currentNote-ot
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
            noteTextArea.setText(NoteController.getInstance().open(selectedFile));
            // létrehozunk egy Note objektumot, amit majd a mentésnél használhatunk
            Path path = Paths.get(selectedFile.getAbsolutePath());
            setCurrentNote(new Note(selectedFile.getName(), path.getParent().toString()));
        } else {
            Utils.showWarning("Nem választott fájlt");
        }
    }

    public MainWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }
}
