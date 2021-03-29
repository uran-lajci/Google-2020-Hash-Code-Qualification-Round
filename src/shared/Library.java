package shared;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
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

    //TODO: ACTUALLY TEST THIS, GOOD LUCK
    /**
     *
     * @param alreadyScannedBooks books already scanned (they won't count towards the score)
     * @param daysLeft days left until the end
     * @return Pair with list of the library's books being scanned as the key and the total score as the value
     */
    public Pair<List<Book>, Integer> getBooksAndScore(List<Book> alreadyScannedBooks, int daysLeft){
        //get days available to scan books
        daysLeft-= signUpTime;

        if (daysLeft <= 0){
            return new Pair<>(new ArrayList<>(), 0);
        }

        //books scanned in the library
        List<Book> usedBooks = new ArrayList<>();

        //total score of the scanned books
        int score = 0;

        //sort books by score (higher first)
        books.sort(Comparator.comparing(b -> b.score, Collections.reverseOrder()));
        List<Book> orderedLibraryBooks = new ArrayList<>(books);

        //remove all books previously scanned by other libraries, since their score wont count
        orderedLibraryBooks.removeAll(alreadyScannedBooks);

        //calculate number of possible scans
        int numberOfPossibleScans = daysLeft * throughput;

        //choose book for every scan
        for (int i = 0; i < numberOfPossibleScans; i++){
            //break if there are no more books available
            if (orderedLibraryBooks.isEmpty()) {
                break;
            }
            Book chosenBook = orderedLibraryBooks.remove(0);
            usedBooks.add(chosenBook);
            score+= chosenBook.score;
        }
        return new Pair<>(usedBooks, score);
    }
}
