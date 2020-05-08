package com.tomiohl.somenotefx.dao;

import com.tomiohl.somenotefx.model.Note;

import java.util.List;

public interface NoteDAO {

    boolean add(Note n);

    boolean delete(Note n);

    List<Note> getAll();
}
