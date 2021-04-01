import java.awt.*;

import ga.GeneticAlgorithm;
import shared.Data;
import shared.FileReader;

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
                    System.out.println("Insert the minimum population size:");
                    String input = scan.nextLine();
                    int minimumPopulationSize = Integer.parseInt(input);

                    System.out.println("Insert the procreation factor:");
                    input = scan.nextLine();
                    double procreationFactor = Double.parseDouble(input);

                    System.out.println("Insert the mutation rate:");
                    input = scan.nextLine();
                    double mutationRate = Double.parseDouble(input);

                    System.out.println("Insert the maximum number of generations:");
                    input = scan.nextLine();
                    int maxSteps = Integer.parseInt(input);

                    System.out.println("Insert the mutation type:");
                    String mutationType = scan.nextLine().trim();

                    long startTime = System.nanoTime();

                    System.out.println("Generating initial population...\n");

                    GeneticAlgorithm ga = new GeneticAlgorithm(procreationFactor, mutationRate, maxSteps, data, mutationType, minimumPopulationSize);

                    System.out.println("Population generated. Starting evolution...");

                    while (ga.getCurrentGeneration() < maxSteps){
                        ga.algorithmStep();
                    }

                    long endTime = System.nanoTime();
                    long timeElapsed = (endTime - startTime) / 1000000;

                    System.out.println("Finished evolution.\n");

                    String result = String.format("Best score: %d points in %d seconds.", ga.getCurrentBestSolution().updateScore(), timeElapsed);
                    System.out.println(result);
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
        System.out.printf("1 - Genetic Algorithm\n2 - Algorithm 2\n3 - Algorithm 3\n0 - Exit");
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
