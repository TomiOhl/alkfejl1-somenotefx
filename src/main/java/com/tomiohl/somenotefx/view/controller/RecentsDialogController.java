package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.App;
import com.tomiohl.somenotefx.model.Note;
import com.tomiohl.somenotefx.controller.NoteController;
import com.tomiohl.somenotefx.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecentsDialogController implements Initializable {

    @FXML
    private TableView<Note> table;

    @FXML
    private TableColumn<Note, String> nameCol;

    @FXML
    private TableColumn<Note, String> dateCol;

    @FXML
    private TableColumn<Note, Void> actionsCol;

    @FXML
    public void closeDialog(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void refreshTable(){
        List<Note> list = NoteController.getInstance().getAll();
        table.setItems(FXCollections.observableList(list));
    }

    public RecentsDialogController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Note> list = NoteController.getInstance().getAll();
        table.setItems(FXCollections.observableList(list));

        nameCol.setCellValueFactory(new PropertyValueFactory<>("filename"));
        dateCol.setCellValueFactory(saveDate -> {
            long saveDateValue = saveDate.getValue().getSaveDate();
            // mivel long, illetve unix időként van tárolva, át kell konvertálni
            StringProperty property = new SimpleStringProperty();
            if (saveDateValue == 0) {
                property.setValue("Még nem mentette");   // itt jöttem rá, hogy létezik File.lastModified(), akkor nem kellett volna tárolni se a saveDate-et, de mostmár marad
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
                property.setValue(dateFormat.format(saveDateValue));
            }
            return property;
        });

        actionsCol.setCellFactory(param -> new TableCell<>() {

            Button deleteButton = new Button("Eltávolítás");

            {   //init blokk
                deleteButton.setOnAction(event -> {
                    Note n = param.getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Biztosan eltávolítja ezt az előzmények közül: " + n.getFilename() + "?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = alert.showAndWait();
                    result.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            boolean success = NoteController.getInstance().deleteFromRecents(n);
                            refreshTable();
                            if (!success)
                                Utils.showError("Az eltávolítás nem sikerült");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }

        });

        table.setOnMouseClicked(event -> {
            Note note = table.getSelectionModel().getSelectedItem();
            if (note != null) {
                // beolvassuk a kiválasztott fájlt
                File f = new File(Path.of(note.getFilePath(), note.getFilename()).toString());
                TextArea noteTextArea = (TextArea) App.getMainStage().getScene().lookup("#noteTextArea");
                noteTextArea.setText(NoteController.getInstance().open(f));
                App.getMainStage().setTitle("SomeNotesFX - " + note.getFilename());
                // beállítjuk a currentNote-ot a választottra
                NoteController.getInstance().setCurrentNote(note);
                // bezárjuk az ablakot
                Stage stage = (Stage) table.getScene().getWindow();
                stage.close();
            }
        });

    }

}
