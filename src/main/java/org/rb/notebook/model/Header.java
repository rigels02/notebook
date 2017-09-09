
package org.rb.notebook.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * V3
 * @author raitis
 */
public class Header implements Serializable, IDataStreamer{

    //unique id for header, user is not responsible
    private long id;
    
    private String title;
    
   //User is not responsible about modTime
    private Date modTime;    

    public Header() {
        this.title="";
        
    }

    public Header(long id, String title, Date modTime) {
        this.id = id;
        this.title = title;
        this.modTime = modTime;
    }

    public Header(long id, String title) {
        this.id = id;
        this.title = title;
       
    }
    public Header(String title) {
      this.title = title;
       
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    public void copyHeader(Header other){
     this.id = other.getId();
     this.title= other.getTitle();
     this.modTime= other.getModTime();
    }
    
   
    @Override
    public String toString() {
        return "Header{" + "id=" + id +", title=" + title + ", modTime=" + modTime + '}';
    }

   @Override
    public void writeData(DataOutputStream dos) throws IOException{
      dos.writeLong(id);
      //raf.writeLong(fpos); //remove- do not keep in data file, it is kept in IndexFile
      
      dos.writeUTF(title);
      dos.writeLong(modTime.getTime());
      
    }
    
    @Override
    public void readData(DataInputStream dis) throws IOException{
      id= dis.readLong();
     
      title= dis.readUTF();
        Date dd = new Date();
        dd.setTime(dis.readLong());
      modTime = dd;
      
    }
    
    
}
