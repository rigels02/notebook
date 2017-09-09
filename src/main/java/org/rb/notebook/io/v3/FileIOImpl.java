package org.rb.notebook.io.v3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import org.rb.notebook.model.Header;



/**
 * V3
 *<pre>
 * Implementation of IRWFile<T> for class what is
 * extension of Header.
 * Data is stored as Data i/o streams.
 * <u> The subclass of Header must
 * Override data write/read methods: writeData(), readData().</u>
 * The subclass must have default constructor (IRWFile uses Class<T> instantiation).
 * Header list is stored in index file IDXFILE as stream list (using streamio) 
 * Data T list is stored in data files DATAFILE_id as stream list
 * Every Header has related data T file with access by index id
 * Header fields constraints:
 * long id - data index is unique number of data in the list (unique number in header list)
 *           used also to make data file name for data T with index id
 *          user is not responsible about id , but FileIOImpl does
 * Date modTime in Header - modify data, user is not responsible for it, but FileIOImpl
 * </pre>
 * @author raitis
 */
public class FileIOImpl<T extends Header> implements IRWFile<T>{

     private final static String IDXFILE = "mtable.idx";
    private final static String DATAFILE = "mtable_";
    private final static String DATAEXT = ".dat";
    private final static String MAGICSIGN = "29-07-2017@#$%";
    private Timestamp timeStamp;

    private List<Header> headerLst;
    
    
    /**
     * Factory could be useful
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public FileIOImpl() throws Exception {

        headerLst = new ArrayList<>();
        File file = new File(IDXFILE);
        if (!file.exists()) {
            file.createNewFile();
            headerLst = new ArrayList<>();
            writeHeaderFile();
        }
        headerLst =  readHeaderFile(IDXFILE);
    }
     
     
     private void logError(Level level, String msg, Object ex) {
        Logger.getLogger(FileIOImpl.class.getName()).log(level, msg, ex);
    }
     
    
    private DataOutputStream openDataOutStream(String fileName) throws IOException {
        DataOutputStream dos = null;
        if (fileName != null) {

            try {
                dos = new DataOutputStream(new FileOutputStream(fileName));
                writeHeader(dos);
            } catch (FileNotFoundException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } 
        }
        return dos;
    }
    
    /**
     * Write file's heade info magicsign and timestamp
     *
     * @param dos
     * @throws IOException
     */
    private void writeHeader(DataOutputStream dos) throws IOException {
        dos.writeUTF(MAGICSIGN);
        //TimeStamp is in format 2016-11-16 06:43:19.77
        timeStamp = new Timestamp(System.currentTimeMillis());
        
        dos.writeLong(timeStamp.getTime());

    }
    private void readHeader(DataInputStream dis) throws Exception {
     String magic = dis.readUTF();
        if (!magic.equals(MAGICSIGN)) {
            throw new IIOException("Bad input file's header!");
        }
        
         timeStamp = new Timestamp(dis.readLong());
       
    }
    
    private List<Header> readHeaderFile(String fileName) throws Exception {
        List<Header> ListOfObjects = new ArrayList<>();
     
        
        try (DataInputStream dis = openDataInStream(fileName)) {
            if (dis != null) {
                long sz= dis.readLong();
                for(int i=1; i<= sz; i++){
                 Header header= new Header();
                  header.readData(dis);
                  ListOfObjects.add(header);
                }
            }
            dis.close();
        }

        return ListOfObjects;
    }
    
    private List<T> readDataFile(String fileName, Class<T> cl) throws Exception {
        List<T> ListOfObjects = new ArrayList<>();
     
        
        try (DataInputStream dis = openDataInStream(fileName)) {
            if (dis != null) {
                long sz= dis.readLong();
                for(int i=1; i<= sz; i++){
                 T obj = cl.newInstance();
                  obj.readData(dis);
                  ListOfObjects.add(obj);
                }
            }
            dis.close();
        }

        return ListOfObjects;
    }
    
    private DataInputStream openDataInStream(String fileName) throws Exception {
     DataInputStream dis = null;
        if (fileName != null) {

            try {
                dis = new DataInputStream(new FileInputStream(fileName));
                readHeader(dis);
            } catch (FileNotFoundException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            }
        }
        return dis;  
    }
    
    @Override
    public void persistAll(List<T> data) throws Exception {
        List<Header> headers= new ArrayList<>();
        
        deleteAll();
        long idx=1;
        for (T t : data) {
            Header header = new Header();
            header.setId(idx);
            header.setModTime(new Date());
            header.setTitle(t.getTitle());
            headers.add(header);
            String filename = composeDataFileName(idx);
            idx++;
            try (DataOutputStream dos = openDataOutStream(filename)){
             t.writeData(dos);
            }
        }
        headerLst = headers;
        writeHeaderFile();
    }
    
     private void writeDataFile(String filePath, List<T> objLst) throws IOException {
        DataOutputStream dos = null;

        dos = openDataOutStream(filePath);
        dos.writeLong(objLst.size());
        for (T t : objLst) {
            t.writeData(dos);
        }

        try {
            dos.flush();
            dos.close();
        } catch (IOException ex) {
            logError(Level.SEVERE, null, ex);

        }
       
      }
     private void writeHeaderFile() throws IOException {
        DataOutputStream dos = null;

        dos = openDataOutStream(IDXFILE);
        dos.writeLong(headerLst.size());
         for (Header header : headerLst) {
             header.writeData(dos);
         }
        
        try {
            dos.flush();
            dos.close();
        } catch (IOException ex) {
            logError(Level.SEVERE, null, ex);

        }

    }
     
     private long getUID() {
        long Idx=1;
        HashSet<Long> iset = new HashSet<>();
         for (Header header : headerLst) {
             iset.add(header.getId());
         }
        for (Long el : iset) {
         if(!iset.contains(Idx)) return Idx;
         Idx++;   
        }
        return Idx;
    }
     
     private String composeDataFileName(long Idx) {
        return DATAFILE + Idx + DATAEXT;
    }
     private Header findHeader(long id) {
         for (Header header : headerLst) {
             if(header.getId()==id) return header;
         }
         return null;
     }

    private void removeHeader(long id) {
        for (int i = 0; i < headerLst.size(); i++) {
            if (headerLst.get(i).getId() == id) {
                headerLst.remove(i);

            }
        }
    }
     
    
    //------------------------------Data operations---------------//
    @Override
    public T find(long id, Class<T> cl) throws Exception {
    
        if(!isIdExists(id)) return null;
         String fileName = composeDataFileName(id);
        try (DataInputStream dis = openDataInStream(fileName)){
            T data = cl.newInstance();
            data.readData(dis);
            Header header = findHeader(id);
            data.copyHeader(header);
            return data;
        } 
       
    }

    @Override
    public List<T> findAll(Class<T> cl) throws Exception {
        List<T> objLst = new ArrayList<>();
        for (Header header : headerLst) {
            String fileName = composeDataFileName(header.getId());
        try (DataInputStream dis = openDataInStream(fileName)){
            T data = cl.newInstance();
            data.readData(dis);
            data.copyHeader(header);
            objLst.add(data);
        } 
        }
        return objLst;
    }

    @Override
    public List<Header> getHlist() {
      return Collections.unmodifiableList(headerLst);   
    }

    private boolean isIdExists(long id){
        for (Header header : headerLst) {
            if(header.getId()==id) return true;
        }
        return false;
    }
    
    @Override
    public void persist(T data) throws Exception {
     data.setId(getUID());
         String fileName = composeDataFileName(data.getId());
     try (DataOutputStream dos = openDataOutStream(fileName)) {
         Header header = new Header();
         header.copyHeader(data);
         //header.setId(getUID());
         header.setModTime(new Date());
         headerLst.add(header);
         data.writeData(dos);
        }
       writeHeaderFile();
      
       
    }

    @Override
    public T edit(T data) throws Exception {
      if(!isIdExists(data.getId())  ) throw new RuntimeException("Data Not FOUND!:"+data);
         String fieleName = composeDataFileName(data.getId());
        Header dheader= findHeader(data.getId());
        
        if(dheader==null) return null;
         String fileName = composeDataFileName(data.getId());
        try (DataOutputStream dos = openDataOutStream(fieleName)) {
            
            dheader.copyHeader(data);
            dheader.setModTime(new Date());
            data.writeData(dos);
            data.copyHeader(dheader);
        }
        
        writeHeaderFile();
      return data;
    }

    @Override
    public boolean remove(T data) throws Exception {
     if(!isIdExists(data.getId())  ) throw new RuntimeException("Data Not FOUND!:"+data);
         String fieleName = composeDataFileName(data.getId());
        removeHeader(data.getId());
        File fi = new File(fieleName);
        if(!fi.delete()) return false;
        writeHeaderFile();
      return true;  
    }

    @Override
    public boolean remove(long id) throws Exception {
        if(!isIdExists(id)  ) throw new RuntimeException("Data Not FOUND!:"+id);
         String fieleName = composeDataFileName(id);
        removeHeader(id);
        File fi = new File(fieleName);
        if(!fi.delete()) return false;
        writeHeaderFile();
      return true;
    }

    public boolean deleteAll() {
        for (Header header : headerLst) {
            long idx = header.getId();
            String filename = composeDataFileName(idx);
            File fi = new File(filename);
            if (fi.exists()) {
                if(!fi.delete()) return false;
            }
        }
        File file = new File(IDXFILE);
        if (file.exists()) {
            if( !file.delete()) return false;
        }
        headerLst.clear();
        return true;
    }

    

    
   
    
}
