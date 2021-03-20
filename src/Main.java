import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        FileReader reader = new FileReader();
        System.out.printf("======================= BOOK SCANNING =======================\n\n");

        System.out.printf("Please choose the input file!\n\n");


        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        dialog.dispose();
        Data data = reader.readFile(new File(file));


        System.out.println("File " + file + " read!\n");

        String choice = null;
        Scanner scan = new Scanner(System.in);

        do {
            printMenu();
            choice = scan.nextLine();
            switch (choice) {
                case "1":
                    //Insert algorithm 1
                    System.out.printf("\n\nInsert algorithm 1!");
                    break;
                case "2":
                    //Insert algorithm 2
                    System.out.printf("\n\nInsert algorithm 2!");
                    break;
                case "3":
                    //Insert algorithm 3
                    System.out.printf("\n\nInsert algorithm 3!");
                    break;
                case "0":
                    System.out.printf("\n\nEND!");
                    break;
                default:
                    System.out.printf("\n\nPlease insert a valid option:");
                    break;
            }

        } while (!choice.equals("0"));
    }

    public static void printMenu(){
        System.out.printf("\n\n======================= MENU =======================\n\n");
        System.out.printf("1 - Algorithm 1\n2 - Algorithm 2\n3 - Algorithm 3\n0 - Exit");
        System.out.printf("\n\nPlease select one option:");
    }

    public static void printAlgorithm1(){
        // Insert the prints for algorithm 1
    }

    public static void printAlgorithm2(){
        // Insert the prints for algorithm 2
    }

    public static void printAlgorithm3(){
        // Insert the prints for algorithm 3
    }
}
