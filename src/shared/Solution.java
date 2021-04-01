package shared;

import java.util.*;

public class Solution implements Comparable<Solution> {

    /**
     * Total days
     */
    private int days;

    private int noLibraries;

    private HashMap<Integer, Boolean> scannedBooks;

    private List<Library> libraries;

    private int score;

    public Solution(int days) {
        this.days = days;
        this.libraries = new ArrayList<>();
        this.scannedBooks = new HashMap<>();
        this.noLibraries = 0;
        this.score = 0;
    }

    public Solution(Solution solution) {
        this.days = solution.days;
        this.libraries = new ArrayList<>(solution.libraries);
        this.noLibraries = solution.noLibraries;
        this.scannedBooks = new HashMap<>(solution.scannedBooks);
        this.score = solution.score;
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

    public int getScore() {
        return score;
    }

    // IMPORTANT!!! MUST BE CALLED IN ORDER TO UPDATE THE SCORE. IF CHANGES WERE MADE TO THE LIBRARIES THE SCORE WILL BE WRONG UNTIL CALLED
    public int updateScore() {
        int currentlyAvailableDays = this.days;
        int score = 0;
        this.resetScans();

        for (Library library : libraries) {
            int libraryScore = library.getScore(this.scannedBooks, currentlyAvailableDays);
            score += libraryScore;
            currentlyAvailableDays -= library.signUpTime;
        }
        this.score = score;
        return score;
    }

    public void addLibrary(Library library) {
        if (!libraries.contains(library)) {
            this.libraries.add(library);
            for (Book book : library.books) {
                this.scannedBooks.put(book.id, false);
            }
            this.noLibraries++;
        }
    }

    public void resetScans() {
        this.scannedBooks.replaceAll((k, v) -> v = false);
    }

    public int getSignUpTime() {
        int totalSignUpTime = 0;
        for (Library library : libraries) {
            totalSignUpTime += library.signUpTime;
        }
        return totalSignUpTime;
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
        while (this.getSignUpTime() > this.days) {
            this.getLibraries().remove(this.noLibraries - 1);
            this.noLibraries--;
        }
    }

    @Override
    public int compareTo(Solution o) {
        return this.score - o.score;
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
