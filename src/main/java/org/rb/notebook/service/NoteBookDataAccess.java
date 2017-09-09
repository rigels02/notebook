package org.rb.notebook.service;

import java.util.List;
import org.rb.notebook.io.v3.IRWFile;
import org.rb.notebook.model.Header;
import org.rb.notebook.model.NotesBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raitis
 */
@Service
//@Scope(value="prototype")
public class NoteBookDataAccess implements IRWFile<NotesBook>{
    
     @Autowired
    IRWFile<NotesBook> repo;

    @Override
    public NotesBook find(long id, Class<NotesBook> cl) throws Exception {
       return repo.find(id, cl);
    }

    
    public List<NotesBook> findAllTest(Class<NotesBook> cl) throws Exception {
        /***/
        System.out.println("\nfindAll()- Thread: "+Thread.currentThread().getName()+" time: "
                +System.currentTimeMillis());
        for(int i=0; i<1000; i++){
        Thread.sleep(15);//0.03*1000
        
            System.out.print(""+Thread.currentThread().getId()+":");
    }
        System.out.println("\nfindAll()- Thread: "+Thread.currentThread().getName()+" Passed");
        /****/
           return repo.findAll(cl);
    }
    
    @Override
    public List<NotesBook> findAll(Class<NotesBook> cl) throws Exception {
        
           return repo.findAll(cl);
    }

    @Override
    public List<Header> getHlist() {
      return repo.getHlist();
    }

    @Override
    public synchronized void persist(NotesBook data) throws Exception {
       repo.persist(data);
    }

    public synchronized void persistAllTest(List<NotesBook> data) throws Exception {
        /***/
        System.out.println("\npersistAll- Thread: "+Thread.currentThread().getName()+" time: "
                +System.currentTimeMillis());
        for(int i=0; i<1000; i++){
        Thread.sleep(15);//0.03*1000
        
            System.out.print(""+Thread.currentThread().getId()+":");
    }
        System.out.println("\npersistAll- Thread: "+Thread.currentThread().getName()+" Passed");
        /****/
      repo.persistAll(data);
    }
    
    
    @Override
    public synchronized void persistAll(List<NotesBook> data) throws Exception {
        /***
        System.out.println("\npersistAll- Thread: "+Thread.currentThread().getName()+" time: "
                +System.currentTimeMillis());
        for(int i=0; i<1000; i++){
        Thread.sleep(15);//0.03*1000
        
            System.out.print(""+Thread.currentThread().getId()+":");
    }
        System.out.println("\npersistAll- Thread: "+Thread.currentThread().getName()+" Passed");
        ****/
      repo.persistAll(data);
    }

    @Override
    public synchronized NotesBook edit(NotesBook data) throws Exception {
     return repo.edit(data);
    }

    @Override
    public synchronized boolean remove(NotesBook data) throws Exception {
        return repo.remove(data);
    }

    @Override
    public synchronized boolean remove(long id) throws Exception {
        return repo.remove(id);
    }
     
     
}
