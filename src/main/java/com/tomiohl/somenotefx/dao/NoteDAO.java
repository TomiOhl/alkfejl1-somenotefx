package com.tomiohl.somenotefx.dao;

import com.tomiohl.somenotefx.model.Note;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface NoteDAO {

    boolean add(Note n);

    boolean deleteFromRecents(Note n);

    boolean verifyIfText(Path path);

    String open(File file);

    boolean save(Note note);

    boolean saveToStorage(String path, String content);

    List<Note> getAll();

    void deleteOldRecents();
}
