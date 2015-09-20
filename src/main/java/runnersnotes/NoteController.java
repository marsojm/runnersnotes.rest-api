package runnersnotes;

import fi.github.marsojm.runnersnotes.boundary.*;
import fi.github.marsojm.runnersnotes.core.interactors.EntityValidationException;
import fi.github.marsojm.runnersnotes.core.interactors.NoteInteractor;
import fi.github.marsojm.runnersnotes.gateway.boundaries.InvalidIdException;
import fi.github.marsojm.runnersnotes.gateway.boundaries.InvalidParentIdException;
import fi.github.marsojm.runnersnotes.gateway.boundaries.NoteGateway;
import fi.github.marsojm.runnersnotes.gateway.implementations.InMemoryDb;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;


@RestController
public class NoteController {
    private static NoteGateway<NoteData> db;

    static {
        InMemoryDb imdb = new InMemoryDb();
        if (imdb.getUser(1) == null) {
            try {
                imdb.createUser(1, new UserData(1,"abcd","abcd1234"));
            } catch (InvalidIdException e) {
                e.printStackTrace();
            }
        }
    }


    public NoteController() {


        db = new InMemoryDb();
        try {
            db.createNote(1,1, new NoteData(1, new Date(), 30.2f, 20, "Had a nice run!"));
            db.createNote(1,2, new NoteData(2, new Date(), 50.0f, 220, "Had a really nice run!"));
            db.createNote(1,3, new NoteData(3, new Date(), 100.0f, 30, "Run of my life!"));
        } catch (InvalidIdException e) {
            e.printStackTrace();
        } catch (InvalidParentIdException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "{userId}/notes/{noteId}", method = RequestMethod.GET)
    public NoteData getNote(@PathVariable int userId, @PathVariable int noteId) {
        NoteBoundary interactor = new NoteInteractor(db);
        return interactor.getNote(new GetNoteRequest(userId, noteId));
    }

    @RequestMapping(value = "{userId}/notes", method = RequestMethod.GET)
    public NoteListDto getNotes(@PathVariable int userId) {
        NoteBoundary interactor = new NoteInteractor(db);
        return new NoteListDto(interactor.getNoteList(new GetNoteListRequest(userId)));
    }

    @RequestMapping(value = "{userId}/notes", method = RequestMethod.POST)
    public ResponseEntity<?> createNote(@PathVariable int userId, @RequestBody CreateNoteRequest request) {
        NoteBoundary interactor = new NoteInteractor(db);
        request.setUserId(userId);
        int id = 0;
        try {
            id = interactor.createNote(request);
        } catch (EntityValidationException e) {
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(e.getErrors(), httpHeaders, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);

    }
}
