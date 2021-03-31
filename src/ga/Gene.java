package ga;

import javafx.util.Pair;
import shared.Book;
import shared.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gene implements Comparable<Gene>{

    /**
     * Total days
     */
    private int days;

    private int noLibraries;

    private List<Library> libraries;

    public Gene(int days){
        this.days = days;
    }

    public Gene(List<Library> libraries, int days){
        this.days = days;
        this.libraries = libraries;
        this.noLibraries = libraries.size();
    }

    public Gene(Gene gene){
        this.days = gene.days;
        this.libraries = new ArrayList<>(gene.libraries);
        this.noLibraries = gene.noLibraries;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public int getNoLibraries() {
        return noLibraries;
    }

    public int getDays() {
        return days;
    }

    public int calculateScore() {
        int currentlyAvailableDays = this.days;
        int score = 0;
        List<Book> usedBooks = new ArrayList<>();
        for (Library library : libraries) {
            Pair<List<Book>, Integer> booksAndScore = library.getBooksAndScore(usedBooks, currentlyAvailableDays);
            List<Book> currentBooks = booksAndScore.getKey();
            score+= booksAndScore.getValue();
            usedBooks.addAll(currentBooks);
            currentlyAvailableDays-= library.signUpTime;
        }
        return score;
    }

    public void addLibrary(Library library){
        this.libraries.add(library);
    }

    public int getSignupTime(){
        int totalSignupTime = 0;
        for (Library library: libraries){
            totalSignupTime+= library.signUpTime;
        }
        return totalSignupTime;
    }

    public void cleanLibraries(){
        //remove duplicates
        List<Library> newList = new ArrayList<>();
           for (Library element : this.libraries) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        this.libraries = newList;

        //remove libraries until sign up time constraint is met
        while (this.getSignupTime() > this.days){
            this.getLibraries().remove(this.noLibraries - 1);
        }
    }

    @Override
    public int compareTo(Gene o) {
        return this.calculateScore() - o.calculateScore();
    }
}
