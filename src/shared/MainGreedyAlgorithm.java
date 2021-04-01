package shared;

import shared.Book;
import shared.Data;
import shared.FileReader;
import shared.Library;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainGreedyAlgorithm {

    public static List<Library> chosenLibraries = new ArrayList<Library>();
    static List<Book> usedBooks = new ArrayList<>();

    public static void greedyAlgorithm(File file, int deadline) throws Exception {

        FileReader reader = new FileReader();
        Data data = reader.readFile(file);
        FileReader.readFile(file);


        data.libraries.sort(new Comparator<Library>() {
            @Override
            public int compare(Library a, Library b) {
                return (b.totalScore / b.signUpTime) - (a.totalScore / a.signUpTime);
            }
        });

        int totalSignup = 0;
        int index = 0;
        while (index < data.libraries.size()) {

            if (totalSignup + data.libraries.get(index).signUpTime > deadline) { //if deadline is > then sig up - break
                break;
            }
            totalSignup += data.libraries.get(index).signUpTime; //calculation of total signup time for the library
            chosenLibraries.add(data.libraries.get(index)); //adding library to the chosenLibraries list
            index++;
        }

        int progress = 0;

        for (Library lib : chosenLibraries) { //iteration over chosenLibraries

            progress += lib.signUpTime;
            if (deadline - progress > 0 && lib.throughput > 0) { //checking if sign up time & throughput is > 0
                int bookSendCapacity = (deadline - progress) * lib.throughput; //calculating send capacity of each lib

                for (int j = 0; j < lib.books.size(); j++) {

                    //check if book has been scanned already
                    if (usedBooks.contains(lib.books.get(j))) { //if book was scanned before
                        lib.books.remove(j); //removes book
                        j--;
                    } else {
                        //adding scanned book to used books
                        usedBooks.add(lib.books.get(j));
                    }
                }

                for (int i = 0; i < lib.books.size() && i <= bookSendCapacity; i++) {
                    lib.chosenBooks.add(lib.books.get(i)); //adding books to be scanned from chosen library to the chosen books list
//                    System.out.println("chosen book: " + lib.chosenBooks.get(i));
                }
            }

        }
        System.out.println("\nused books");
        for (Book usedBook : usedBooks) {
            System.out.println(usedBook + " ");
        }

        int score = 0;
        for (Library lib : chosenLibraries) {
            for (Book book : lib.chosenBooks) {
                score += book.score;
            }
            System.out.println();
        }
        System.out.println("score = " + score);

    }
}