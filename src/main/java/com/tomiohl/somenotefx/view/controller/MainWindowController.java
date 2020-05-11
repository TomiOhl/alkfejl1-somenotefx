package com.tomiohl.somenotefx.view.controller;

import com.tomiohl.somenotefx.App;
import com.tomiohl.somenotefx.controller.NoteController;
import com.tomiohl.somenotefx.model.Note;
import com.tomiohl.somenotefx.utils.ConverterUtils;
import com.tomiohl.somenotefx.utils.DialogUtils;
import javafx.concurrent.Task;
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
                if (!NoteController.getInstance().saveToStorage(path, noteTextArea.getText()))
                    DialogUtils.showError("A mentés nem sikerült");
            } else {
                DialogUtils.showError("A mentés nem sikerült");
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
            NoteController.getInstance().deleteOldRecents();
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
    public void openFile() {
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
                    DialogUtils.showWarning("A fájl nem .md vagy .txt kiterjesztésű");
                }
                noteTextArea.setText(NoteController.getInstance().open(selectedFile));
                App.getMainStage().setTitle("SomeNotesFX - " + fileName);
            } else {
                DialogUtils.showError("A fájl nem szövegfájl");
                return;
            }
            // létrehozzuk az új currentNote-ot
            Note currentNote = new Note(fileName, path.getParent().toString());
            NoteController.getInstance().setCurrentNote(currentNote);
            NoteController.getInstance().save(currentNote);
        } else {
            DialogUtils.showWarning("Nem választott fájlt");
        }
    }

    @FXML
    public void newNote() {
        NoteController.getInstance().setCurrentNote(null);
        App.getMainStage().setTitle("SomeNotesFX - Névtelen");
        noteTextArea.clear();
    }

    @FXML
    public void previewNote() {
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

    public void exportToPdf() {
        saveNote();
        Note currentNote = NoteController.getInstance().getCurrentNote();
        Task<String> exportTask = new Task<>() {
            @Override
            protected String call() {
                String html = ConverterUtils.convertToHtml();
                return ConverterUtils.convertToPdf(html);
            }
        };
        // futás közben ezt jelezzük a címsoron
        exportTask.setOnRunning(event ->
                App.getMainStage().setTitle("SomeNotesFX - " + currentNote.getFilename() + " exportálása folyamatban..."));
        // siker esetén tájékoztatás
        exportTask.setOnSucceeded(event -> {
            App.getMainStage().setTitle("SomeNotesFX - " + currentNote.getFilename());
            // kérjük el a Task eredményeként kapot stringet
            final String pdfFile = (String) event.getSource().getValue();
            if (pdfFile != null)
                DialogUtils.showSuccess("A fájl sikeresen exportálva. Megtalálható itt: " + pdfFile);
            else
                DialogUtils.showError("Ismeretlen hiba. Hogy őszinte legyek, ide sose lenne szabad eljutni. Nagyon sajnálom");
        });
        // hiba esetén error dialog
        exportTask.setOnFailed(event -> {
            App.getMainStage().setTitle("SomeNotesFX - " + currentNote.getFilename());
            Exception e = (Exception) event.getSource().getException();
            // ha nullpointer volt, akkor valószínűleg simán csak üres volt a fájlunk
            if (e instanceof NullPointerException)
                DialogUtils.showWarning("Először jegyzeteljen valamit, utána exportálja");
            else
                DialogUtils.showError("Hiba történt: " + e.toString());
        });
        new Thread(exportTask).start();    // külön szálon végezzük
    }

    public MainWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
