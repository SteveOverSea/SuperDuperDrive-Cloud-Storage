package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void save(Note note) {
        noteMapper.saveNote(note);
    }

    public void update(Note note) {
        noteMapper.updateNote(note);
    }

    public void delete(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public ArrayList<Note> getNotes(Integer userid) {
        return noteMapper.getAllNotes(userid);
    }
}
