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
                    Utils.showError("A mentés nem sikerült");
                }
                System.out.println(currentNote);
            } else {
                Utils.showError("A mentés nem sikerült");
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
        FileChooser.ExtensionFilter supportedFilter =
                new FileChooser.ExtensionFilter(".txt, .md", "*.txt", "*.md");
        FileChooser.ExtensionFilter noFilter = new FileChooser.ExtensionFilter("Minden fájl", "*");
        chooser.getExtensionFilters().addAll(supportedFilter, noFilter);
        // választó ablak
        File selectedFile = chooser.showOpenDialog(App.getMainStage());
        // beolvassuk a fájlt
        if (selectedFile != null) {
            Path path = Paths.get(selectedFile.getAbsolutePath());
            String fileName = selectedFile.getName();
            // ellenőrizzűk, szövegfájl-e
            if (NoteController.getInstance().verifyIfText(path)) {
                // ellenőrizzük, hogy támogatott-e, tehát md vagy txt
                int extensionFrom = fileName.lastIndexOf(".") + 1;
                String extension = fileName.substring(extensionFrom);
                if (!
                    (extension.equalsIgnoreCase("md") ||
                     extension.equalsIgnoreCase("txt") )
                ) {
                    Utils.showWarning("A fájl nem .md vagy .txt kiterjesztésű");
                }
                noteTextArea.setText(NoteController.getInstance().open(selectedFile));
            } else {
                Utils.showError("A fájl nem szövegfájl");
                return;
            }
            // létrehozzuk az új currentNote-ot
            Note currentNote = new Note(fileName, path.getParent().toString());
            NoteController.getInstance().setCurrentNote(currentNote);
            NoteController.getInstance().save(currentNote);
        } else {
            Utils.showWarning("Nem választott fájlt");
        }
    }

    @FXML
    public void newNote(ActionEvent actionEvent) {
        App app = new App();
        app.start(new Stage());  // csakhogy a currentNote ugyanaz itt is, ott is
    }

    @FXML
    public void previewNote(ActionEvent actionEvent) {
        saveNote(); // mivel a tárhelyről lesz beolvasva az előnézet
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/tomiohl/somenotefx/view/preview_window.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Előnézet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
