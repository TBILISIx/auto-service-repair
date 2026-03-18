package autoservice.repair.services;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Document {

    private final Integer id;
    private final LocalDate date;

    public Document(Integer id) {
        this.id = id;
        this.date = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
