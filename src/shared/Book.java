package shared;

import java.util.Objects;

public class Book implements Comparable<Book>{

    int id;
    int score;

    public static int idCounter;

    public Book(int score){
        this.id = idCounter++;
        this.score = score;
    }

    public Book(Book b){
        this.id = b.id;
        this.score = b.score;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return getId() == book.getId() && score == book.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), score);
    }

    @Override
    public int compareTo(Book o) {
        return this.score - o.score;
    }
}
