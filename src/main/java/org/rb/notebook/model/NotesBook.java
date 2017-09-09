
package org.rb.notebook.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author raitis
 */
public class NotesBook extends Header implements IDataStreamer{
    
    private List<Note> notes;

    public NotesBook() {
        super();
        notes= new ArrayList<>();
                
    }

    public NotesBook(String title) {
       super(title);
       notes= new ArrayList<>();
    }

    public NotesBook(List<Note> notes,String title) {
        super(title);
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public void readData(DataInputStream dis) throws IOException {
        // super.readData(dis);
        int sz = dis.readInt();
        for (int i = 0; i < sz; i++) {
            Note note = new Note();
            note.readData(dis);
            notes.add(note);
        }
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
       // super.writeData(dos); 
       dos.writeInt(notes.size());
        for (Note note : notes) {
            note.writeData(dos);
        }
    }

    
    
    @Override
    public String toString() {
        return "NotesBook{"+super.toString() + "notes=" + notes + '}';
    }
    
}
