import java.awt.*;

import ga.GeneticAlgorithm;
import shared.*;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        FileReader reader = new FileReader();
        System.out.printf("======================= BOOK SCANNING =======================\n\n");

        System.out.printf("Please choose the input file!\n\n");


        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
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

                    while (ga.getCurrentGeneration() < maxSteps) {
                        ga.algorithmStep();
                    }

                    long endTime = System.nanoTime();
                    long timeElapsed = (endTime - startTime) / 1000000;

                    System.out.println("Finished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    ga.getCurrentBestSolution().exportFile(input);

                    String result = String.format("\nBest score: %d points in %d milliseconds.", ga.getCurrentBestSolution().getScore(), timeElapsed);
                    System.out.println(result);
                    break;
                case "2":

                    long startTime2 = System.nanoTime();
                    Solution solution = MainGreedyAlgorithm.greedyAlgorithm(data, data.noDays);
                    long endTime2 = System.nanoTime();
                    long timeElapsed2 = (endTime2 - startTime2) / 1000000;
                    System.out.println("Finished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    String greedyFileName = scan.nextLine();
                    solution.exportFile(greedyFileName);

                    System.out.printf("Best score: %d points in %d milliseconds.", solution.getScore(), timeElapsed2);

                    break;
                case "3":
                    System.out.printf("\n\nInsert the initial temperature:");
                    String inputSA = scan.nextLine();
                    double temperature = Double.parseDouble(inputSA);

                    System.out.print("\nInsert the cooling factor:");
                    inputSA = scan.nextLine();
                    double coolingFactor = Double.parseDouble(inputSA);

                    long startTimeSA = System.nanoTime();
                    Solution solutionSA = SimulatedAnnealing.simulatedAnnealingAlgorithm(data, temperature, coolingFactor);
                    long endTimeSA = System.nanoTime();
                    long timeElapsedSA = (endTimeSA - startTimeSA) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    String saFileName = scan.nextLine();
                    solutionSA.exportFile(saFileName);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionSA.getScore(), timeElapsedSA);
                    break;

                case "4":
                    System.out.printf("\n\nInsert the maximum number for the neighbor search:");
                    String inputHC = scan.nextLine();
                    int maxNeighbor = Integer.parseInt(inputHC);


                    long startTimeHC = System.nanoTime();
                    Solution solutionHC = HillClimbing.hillClimbingAlgorithm(data, maxNeighbor);
                    long endTimeHC = System.nanoTime();
                    long timeElapsedHC = (endTimeHC - startTimeHC) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    String hcFileName = scan.nextLine();
                    solutionHC.exportFile(hcFileName);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionHC.getScore(), timeElapsedHC);
                    break;
                case "5":
                    System.out.printf("\n\nInsert the desired neighborhood size:");
                    String inputHCSA = scan.nextLine();
                    int neighborhoodSize = Integer.parseInt(inputHCSA);


                    long startTimeHCSA = System.nanoTime();
                    Solution solutionHCSA = HillClimbingSteepestAscent.hillClimbingSteepestAscentAlgorithm(data, neighborhoodSize);
                    long endTimeHCSA = System.nanoTime();
                    long timeElapsedHCSA = (endTimeHCSA - startTimeHCSA) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    String hcsaFileName = scan.nextLine();
                    solutionHCSA.exportFile(hcsaFileName);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionHCSA.getScore(), timeElapsedHCSA);
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

    public static void printMenu() {
        System.out.printf("\n\n======================= MENU =======================\n\n");
        System.out.printf("1 - Genetic Algorithm\n2 - Non-Meta Heuristic Greedy Algorithm\n3 - Simulated Annealing Algorithm\n4 - Hill Climbing Algorithm\n5 - Steepest-Ascent Hill Climbing Algorithm\n0 - Exit");
        System.out.printf("\n\nPlease select one option:");
    }
}
