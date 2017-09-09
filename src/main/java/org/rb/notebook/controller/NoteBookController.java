
package org.rb.notebook.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rb.notebook.io.v3.IRWFile;
import org.rb.notebook.model.Header;
import org.rb.notebook.model.Note;
import org.rb.notebook.model.NotesBook;
import org.rb.notebook.service.NoteBookDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author raitis
 */
@RestController
@RequestMapping("/notes")
//@Scope(value="request")
//@CrossOrigin(origins = "*",allowedHeaders = "*")
public class NoteBookController {
    
    @Autowired
    NoteBookDataAccess noteBookDA;
    
    @RequestMapping(path ="/dummy" ,method = RequestMethod.GET)
    public ResponseEntity<List<Note>> getDummy(){
        NotesBook notesBook = new NotesBook("root");
        for(int i=1; i<= 3; i++){
        notesBook.getNotes().add(new Note("Note_"+i, new Date(), "Content_"+i));
        }
        
        return new ResponseEntity<>(notesBook.getNotes() , HttpStatus.OK);
    }
    
    @RequestMapping(path ="/testr" ,method = RequestMethod.GET)
    public ResponseEntity<String> gettest() throws Exception {

        System.out.println("gettest()- Thread: " + Thread.currentThread().getName() + " time: "
                + System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(15);//0.03*1000

            System.out.print("" + Thread.currentThread().getId() + ":");
        }
        System.out.println("\nDone.");
        return new ResponseEntity<>("Done..", HttpStatus.OK);
    }
    
    
    @RequestMapping(path ="/testsave" ,method = RequestMethod.GET)
    public ResponseEntity<String> getSave() throws Exception{
        NotesBook notesBook = new NotesBook("root");
        for(int i=1; i<= 3; i++){
        notesBook.getNotes().add(new Note("Note_"+i, new Date(), "Content_"+i));
        }
        List<NotesBook> books= new ArrayList<>();
        books.add(notesBook);
        
        System.out.println("\ngetSave()- Thread: " + Thread.currentThread().getName() + " time: "
                + System.currentTimeMillis());
        
        
        noteBookDA.persistAllTest(books);
        
        return new ResponseEntity<>("Saved!" , HttpStatus.OK);
    }
    
    
    
    @RequestMapping(path ="/testread" ,method = RequestMethod.GET)
    public ResponseEntity<List<Note>> getRead() throws Exception{
  
         System.out.println("\ngetRead()- Thread: "+Thread.currentThread().getName()+" time: "+
                 System.currentTimeMillis());
        List<NotesBook> books = noteBookDA.findAllTest(NotesBook.class);
        if(books==null)
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<>(books.get(0).getNotes() , HttpStatus.OK);
    }
    /*********************************************************/
    
   /**
    * 
    * @return
    * @throws Exception 
    */ 
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Note>> getNotes(){
        
        List<NotesBook> books;
        try {
            books = noteBookDA.findAll(NotesBook.class);
        } catch (Exception ex) {
            Logger.getLogger(NoteBookController.class.getName()).log(Level.SEVERE, null, ex);
             return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(books.get(0).getNotes() , HttpStatus.OK);
    }
    
    @RequestMapping(path = "headers",method = RequestMethod.GET)
    public ResponseEntity<List<Header>> getHeaders() {
        
        List<Header> headers = noteBookDA.getHlist();
        
        return new ResponseEntity<>(headers , HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> postNotes(@RequestBody List<Note> notes) {
        List<Header> headers = noteBookDA.getHlist();
        for (Header header : headers) {
           if(header.getTitle().equals("root")){
               try {
                   noteBookDA.remove(header.getId());
                   break;
               } catch (Exception ex) {
                   Logger.getLogger(NoteBookController.class.getName()).log(Level.SEVERE, null, ex);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
               }
           }
        }
        NotesBook noteBook = new NotesBook(notes, "root");
        List<NotesBook> books = new ArrayList<>();
        books.add(noteBook);
        try {
            noteBookDA.persistAll(books);
        } catch (Exception ex) {
            Logger.getLogger(NoteBookController.class.getName()).log(Level.SEVERE, null, ex);
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> remove() {
        List<Header> headers = noteBookDA.getHlist();
        for (Header header : headers) {
            if(header.getTitle().equals("root")){
                try {
                    noteBookDA.remove(header.getId());
                } catch (Exception ex) {
                    Logger.getLogger(NoteBookController.class.getName()).log(Level.SEVERE, null, ex);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
