package autoservice.repair.services;

import java.time.LocalDate;

public abstract class Document {

    private final int id;
    private final LocalDate date;

    public Document(int id) {
        this.id = id;
        this.date = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
