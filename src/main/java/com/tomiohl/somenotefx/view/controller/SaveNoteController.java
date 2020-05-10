package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.App;
import com.tomiohl.somenotefx.model.Note;
import com.tomiohl.somenotefx.utils.Utils;
import com.tomiohl.somenotefx.controller.NoteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class SaveNoteController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private Button okButton;
    @FXML
    private Label selectedDirLabel;

    private Note note = new Note();
    private TextArea noteTextArea;
    private String chosenPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        note.filenameProperty().bind(nameField.textProperty());
        note.filePathProperty().bind(selectedDirLabel.textProperty());
        note.setSaveDate(System.currentTimeMillis());

        okButton.disableProperty().bind( nameField.textProperty().isEmpty()
                                                .or(selectedDirLabel.textProperty().isEmpty()));

        noteTextArea = (TextArea) App.getMainStage().getScene().lookup("#noteTextArea");

    }

    public SaveNoteController() {
    }

    @FXML
    private void save(ActionEvent event) {
        if (NoteController.getInstance().add(note)) {
            // ha mentettük az adatbázisba, mentsűk má' a tárhelyre is, meg legyen ez a current
            NoteController.getInstance().setCurrentNote(note);
            String path = Paths.get(chosenPath, nameField.getText()).toString();
            if (NoteController.getInstance().saveToStorage(path, noteTextArea.getText())) {
                Utils.showSuccess("A mentés sikeres");
            } else {
                Utils.showWarning("Nem sikerült a mentés");
            }
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Utils.showWarning("Nem sikerült a mentés");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Válasszon egy mappát");
        File selectedDirectory = directoryChooser.showDialog(App.getMainStage());
        if (selectedDirectory != null) {
            chosenPath = selectedDirectory.getAbsolutePath();
            selectedDirLabel.setText(chosenPath);
        } else {
            Utils.showWarning("Nem választott mappát");
        }
    }

}
