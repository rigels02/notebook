
package org.rb.notebook.model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author raitis
 */
public interface IDataStreamer {

    void readData(DataInputStream dis) throws IOException;

    void writeData(DataOutputStream dos) throws IOException;
    
}
