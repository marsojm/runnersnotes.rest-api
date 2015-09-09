package runnersnotes;

import fi.github.marsojm.runnersnotes.boundary.NoteData;

import java.util.List;

public class NoteListDto {
    private List<NoteData> items;


    public NoteListDto(List<NoteData> items) {
        this.items = items;
    }

    public List<NoteData> getItems() {
        return items;
    }
}
