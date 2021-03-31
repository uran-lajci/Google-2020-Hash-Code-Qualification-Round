package shared;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solution implements Comparable<Solution> {

    /**
     * Total days
     */
    private int days;

    private int noLibraries;

    private List<Library> libraries;

    public Solution(int days) {
        this.days = days;
        this.libraries = new ArrayList<>();
        this.noLibraries = 0;
    }

    public Solution(List<Library> libraries, int days) {
        this.days = days;
        this.libraries = libraries;
        this.noLibraries = libraries.size();
    }

    public Solution(Solution solution) {
        this.days = solution.days;
        this.libraries = new ArrayList<>(solution.libraries);
        this.noLibraries = solution.noLibraries;
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
            score += booksAndScore.getValue();
            usedBooks.addAll(currentBooks);
            currentlyAvailableDays -= library.signUpTime;
        }
        return score;
    }

    public void addLibrary(Library library) {
        if (!libraries.contains(library)){
            this.libraries.add(library);
            this.noLibraries++;
        }
    }

    public int getSignupTime() {
        int totalSignupTime = 0;
        for (Library library : libraries) {
            totalSignupTime += library.signUpTime;
        }
        return totalSignupTime;
    }

    public void cleanLibraries() {
        //remove duplicates
        List<Library> newList = new ArrayList<>();
        for (Library element : this.libraries) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        this.libraries = newList;
        this.noLibraries = this.libraries.size();

        //remove libraries until sign up time constraint is met
        while (this.getSignupTime() > this.days) {
            this.getLibraries().remove(this.noLibraries - 1);
            this.noLibraries--;
        }
    }

    @Override
    public int compareTo(Solution o) {
        return this.calculateScore() - o.calculateScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return days == solution.days &&
                noLibraries == solution.noLibraries &&
                libraries.equals(solution.libraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(days, noLibraries, libraries);
    }
}
