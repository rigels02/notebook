package org.rb.notebook;

import org.rb.notebook.io.v3.FileIOImpl;
import org.rb.notebook.io.v3.IRWFile;
import org.rb.notebook.model.Header;
import org.rb.notebook.model.NotesBook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotebookApplication {
    
    @Bean
    public IRWFile<NotesBook> setIOFile() throws Exception{
        FileIOImpl<NotesBook> io = new FileIOImpl<>();
        return io;
    }

	public static void main(String[] args) {
		SpringApplication.run(NotebookApplication.class, args);
	}
}
