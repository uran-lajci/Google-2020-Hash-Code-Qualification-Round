package shared;

import javafx.util.Pair;

import java.util.*;

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
     * @param daysLeft days left until the end
     * @return Pair with list of the library's books being scanned as the key and the total score as the value
     */
    public int getScore(HashMap<Integer, Boolean> alreadyScannedBooks, int daysLeft){
        this.chosenBooks = new ArrayList<>();
        //get days available to scan books
        daysLeft-= signUpTime;

        if (daysLeft <= 0){
            return 0;
        }

        //books scanned in the library
        List<Book> usedBooks = new ArrayList<>();

        //total score of the scanned books
        int score = 0;

        //sort books by score (higher first)
        sortBooks();


        //calculate number of possible scans
        int numberOfPossibleScans = daysLeft * throughput;

        List<Book> orderedLibraryBooks = new ArrayList<>(books);

        int scannedBooks = 0;
        //choose book for every scan
        while( scannedBooks < numberOfPossibleScans && orderedLibraryBooks.size() != 0){
            Book chosenBook = orderedLibraryBooks.remove(0);
            if(!alreadyScannedBooks.get(chosenBook.id)){
                alreadyScannedBooks.put(chosenBook.id, true);
                this.chosenBooks.add(chosenBook);
                usedBooks.add(chosenBook);
                score+= chosenBook.score;
                scannedBooks++;
            }

        }
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return id == library.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
