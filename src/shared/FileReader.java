package shared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public FileReader(){
    }

    public static Data readFile(File file) throws Exception{ // throw every exception cause i'm lazy
        List<Book> books = new ArrayList<>();
        List<Library> libraries = new ArrayList<>();

        Scanner reader = new Scanner(file);

        String line = reader.nextLine();

        // First line contains the number of nooks, libraries and days
        String[] values = line.trim().split(" ");

        int noBooks = Integer.parseInt(values[0]);
        int noLibraries = Integer.parseInt(values[1]);
        int noDays = Integer.parseInt(values[2]);

        String[] scores = reader.nextLine().split(" ");

        // Read scores for each book
        for (int i = 0; i < noBooks; i++){
            books.add(new Book(Integer.parseInt(scores[i])));
        }

        // Read number of books, sign up time and throughput of every library, as well as the books in that library
        for (int i = 0; i < noLibraries; i++){
            String[] libraryInfo = reader.nextLine().trim().split(" ");

            int noBooksLibrary = Integer.parseInt(libraryInfo[0]);
            int signUpTime = Integer.parseInt(libraryInfo[1]);
            int throughput = Integer.parseInt(libraryInfo[2]);

            Library library = new Library(noBooksLibrary, signUpTime, throughput);

            String[] libraryBooks = reader.nextLine().trim().split(" ");
            for (int j = 0; j < noBooksLibrary; j++){
                library.books.add(books.get(Integer.parseInt(libraryBooks[j])));
            }
            library.calculateScore();
            library.sortBooks();
            libraries.add(library);

        }

        reader.close();

        Data data = new Data(books, libraries, noDays);
        return data;
    }
}
