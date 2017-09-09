
package org.rb.notebook.io.v3;

import java.util.List;
import org.rb.notebook.model.Header;



/**
 *
 * @author raitis
 * @param <T>
 */
public interface IRWFile<T extends Header> {

    
    T find(long id, Class<T> cl) throws Exception;

    List<T> findAll(Class<T> cl) throws Exception;

    List<Header> getHlist();

    //--------------Data operations -------//
    void persist(T data) throws Exception;

    void persistAll(List<T> data) throws Exception;
     
    T edit(T data) throws Exception;

    boolean remove(T data) throws Exception;

    boolean remove(long id) throws Exception;
    
}
