package com.tomiohl.somenotefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    private void setMainStage(Stage mainStage) {
        App.mainStage = mainStage;
    }

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/tomiohl/somenotefx/view/main_window.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SomeNotesFX - Névtelen");
            stage.show();
            setMainStage(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}