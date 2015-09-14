package runnersnotes;

import fi.github.marsojm.runnersnotes.boundary.*;
import fi.github.marsojm.runnersnotes.core.interactors.NotesInteractor;
import fi.github.marsojm.runnersnotes.gateway.boundaries.InvalidIdException;
import fi.github.marsojm.runnersnotes.gateway.boundaries.NoteGateway;
import fi.github.marsojm.runnersnotes.gateway.implementations.InMemoryDb;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;


@RestController
public class NoteController {
    private static NoteGateway<NoteData> db;

    public NoteController() {
        db = new InMemoryDb();

        try {
            db.createNote(1, new NoteData(1, new Date(), 30.2f, 20, "Had a nice run!"));
            db.createNote(2, new NoteData(2, new Date(), 50.0f, 220, "Had a really nice run!"));
            db.createNote(3, new NoteData(3, new Date(), 100.0f, 30, "Run of my life!"));
        } catch (InvalidIdException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/notes/{noteId}", method = RequestMethod.GET)
    public NoteData getNote(@PathVariable int noteId) {
        NoteBoundary interactor = new NotesInteractor(db);
        return interactor.getNote(new GetNoteRequest(noteId));
    }

    @RequestMapping(value = "/notes", method = RequestMethod.GET)
    public NoteListDto getNotes() {
        NoteBoundary interactor = new NotesInteractor(db);
        return new NoteListDto(interactor.getNoteList(new GetNoteListRequest()));
    }

    @RequestMapping(value = "/notes", method = RequestMethod.POST)
    public ResponseEntity<?> createNote(@RequestBody CreateNoteRequest request) {
        NoteBoundary interactor = new NotesInteractor(db);
        int id = interactor.createNote(request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);

    }
}
