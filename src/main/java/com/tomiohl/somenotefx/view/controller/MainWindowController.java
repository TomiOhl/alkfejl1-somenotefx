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

    @FXML
    private TextArea noteTextArea;

    @FXML
    public void saveNote() {
        Note currentNote = NoteController.getInstance().getCurrentNote();
        if (currentNote != null) {
            NoteController.getInstance().getCurrentNote().setSaveDate(System.currentTimeMillis());
            if (NoteController.getInstance().save(currentNote)) {
                // sikerült menteni az adatbázisba, mentsük a fájlt is
                String path = Paths.get(currentNote.getFilePath(), currentNote.getFilename()).toString();
                if (!NoteController.getInstance().saveToStorage(path, noteTextArea.getText())) {
                    Utils.showWarning("Nem sikerült a mentés");
                }
                System.out.println(currentNote);
            } else {
                Utils.showWarning("A mentés nem sikerült");
            }
        } else {
            // új jegyzetet kezdtünk, így kell neki nevet és helyet találni
            saveNoteAs();
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
            NoteController.getInstance().setCurrentNote(new Note(selectedFile.getName(), path.getParent().toString()));
        } else {
            Utils.showWarning("Nem választott fájlt");
        }
    }

    @FXML
    public void newNote(ActionEvent actionEvent) {
        App app = new App();
        app.start(new Stage());  // csakhogy a currentNote ugyanaz itt is, ott is
    }

    public MainWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
