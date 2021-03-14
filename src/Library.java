import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Library {

    public int id;
    public int bookNumber;
    public int signUpTime;
    public int throughput; //no of books scanned per day

    public List<Book> books;
    public List<Book> chosenBooks;

    public int totalScore;
    public static int idCounter;

    public Library(int bookNumber, int signUpTime, int throughput){
        this.id = idCounter++;
        this.bookNumber = bookNumber;
        this.signUpTime = signUpTime;
        this.throughput = throughput;
        this.books = new ArrayList<Book>();
        this.chosenBooks = new ArrayList<Book>();
    }

    public void calculateScore(){

        int score = 0;
        for (Book book : this.books){
            score += book.score;
        }

        this.totalScore = score;
    }

    public void sortBooks(){

        books.sort(new Comparator<Book>(){
            @Override
            public int compare(Book a, Book b){
                return b.score - a.score;
            }
        });
    }
}
