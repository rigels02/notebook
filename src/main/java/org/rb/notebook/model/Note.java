
package org.rb.notebook.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author raitis
 */
public class Note implements Serializable,IDataStreamer{
    
    private String title;
    private String note;
    private Date modTime;
    
    public Note() {
       
        title="";
        note="";
    }

    public Note(String title,Date modTime,String content) {
        this.title=title;
        this.modTime= modTime;
        this.note = content;
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

    
    public String getNote() {
        return note;
    }

    public void setNote(String content) {
        this.note = content;
    }

    @Override
    public String toString() {
        return "Note{" + "title=" + title + ", modTime=" + modTime + ", content=" + note + '}';
    }

    

   @Override
    public void readData(DataInputStream dis) throws IOException {
        //super.readData(dis); 
        this.title= dis.readUTF();
        this.modTime= new Date(dis.readLong());
        note = dis.readUTF();
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        //super.writeData(dos); 
        dos.writeUTF(title);
        dos.writeLong(modTime.getTime());
        dos.writeUTF(note);
    }
   
    
    
}
