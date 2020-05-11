package com.tomiohl.somenotefx.controller;

import com.tomiohl.somenotefx.dao.NoteDAO;
import com.tomiohl.somenotefx.dao.NoteDaoImpl;
import com.tomiohl.somenotefx.model.Note;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class NoteController {

    private Note currentNote;
    private NoteDAO dao = new NoteDaoImpl();
    private static NoteController single_instance = null;

    private NoteController() {

    }

    public static NoteController getInstance(){     // nem feltétlen helyes, ha egyszerre tobbet akarunk szerkeszteni
        if(single_instance == null){
            single_instance = new NoteController();
        }
        return single_instance;
    }

    public boolean add(Note note) {
        return dao.add(note);
    }

    public boolean deleteFromRecents(Note note) {
        return dao.deleteFromRecents(note);
    }

    public boolean verifyIfText(Path path) {
        return dao.verifyIfText(path);
    }

    public String open(File file) {
        return dao.open(file);
    }

    public boolean save(Note note) {
        return dao.save(note);
    }

    public boolean saveToStorage(String path, String content) {
        return dao.saveToStorage(path, content);
    }

    public List<Note> getAll() {
        return dao.getAll();
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }

}
