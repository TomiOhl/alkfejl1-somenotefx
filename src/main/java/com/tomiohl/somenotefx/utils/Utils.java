package com.tomiohl.somenotefx.utils;

import javafx.scene.control.Alert;

public class Utils {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Figyelmeztetés");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("A művelet sikeresen teljesítve");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
