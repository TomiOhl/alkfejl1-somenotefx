package com.tomiohl.somenotefx.controller;

import com.tomiohl.somenotefx.dao.NoteDAO;
import com.tomiohl.somenotefx.dao.NoteDaoImpl;
import com.tomiohl.somenotefx.model.Note;

import java.io.File;
import java.util.List;

public class NoteController {

    private NoteDAO dao = new NoteDaoImpl();
    private static NoteController single_instance = null;

    private NoteController() {

    }

    public static NoteController getInstance(){
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

    public String open(File file) {
        return dao.open(file);
    }

    public List<Note> getAll() {
        return dao.getAll();
    }
}
