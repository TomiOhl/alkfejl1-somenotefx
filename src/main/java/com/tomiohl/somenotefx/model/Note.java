package com.tomiohl.somenotefx.model;

import javafx.beans.property.*;

public class Note {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty filename = new SimpleStringProperty();
    private LongProperty saveDate = new SimpleLongProperty();
    private StringProperty filePath = new SimpleStringProperty();

    public Note(int id, String filename, String filePath, long saveDate) {
        this.id.set(id);
        this.filename.set(filename);
        this.saveDate.set(saveDate);
        this.filePath.set(filePath);
    }

    public Note(String filename, String filePath) {
        this.filename.set(filename);
        this.filePath.set(filePath);
    }

    @Override
    public String toString() {
        return "Jegyzet{" +
                "id=" + id.get() +
                ", filename=" + filename.get() +
                ", savedate=" + saveDate.get() +
                ", filepath=" + filePath.get() +
                '}';
    }

    public Note() {
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFilename() {
        return filename.get();
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename.set(filename);
    }

    public long getSaveDate() {
        return saveDate.get();
    }

    public LongProperty saveDateProperty() {
        return saveDate;
    }

    public void setSaveDate(long saveDate) {
        this.saveDate.set(saveDate);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }
}
