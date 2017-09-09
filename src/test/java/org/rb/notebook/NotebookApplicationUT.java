package org.rb.notebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rb.notebook.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ********************************************
 * TO TEST RUN SPRING APPLICATION FIRST !!!
 * ********************************************
 * @author raitis
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotebookApplicationUT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void readNotes() {

        ResponseEntity<Note[]> entity = this.restTemplate.getForEntity("/notes", Note[].class);

        int sz = entity.getBody().length;
        Note[] notes1 = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(sz == 3);
    }

    @Test
    public void getNotes() {
        List<Note> notes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            notes.add(new Note("Note_" + i, new Date(), "Content_" + i));
        }
        Note[] anotes= new Note[notes.size()];
        notes.toArray(anotes);
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Note>> requestEntity = new HttpEntity(notes,headers);
        //ResponseEntity<HttpStatus> response = this.restTemplate.postForEntity("/notes", notes, HttpStatus.class);
        ResponseEntity<HttpStatus> response = restTemplate.exchange("/notes", HttpMethod.POST, requestEntity, HttpStatus.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Note[]> entity = this.restTemplate.getForEntity("/notes", Note[].class);

        int sz = entity.getBody().length;
        Note[] notes1 = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(sz == 3);
        boolean ok=false;
        int idx=0;
        for (Note anote : anotes) {
            ok=anote.toString().equals(notes1[idx++].toString());
            assertTrue(ok);
        }
    }

}
