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

        String input = "";
        long startTime, endTime, timeElapsed = 0;

        do {
            printMenu();
            choice = scan.nextLine();
            switch (choice) {
                case "1":
                    startTime = System.nanoTime();
                    Solution solution = MainGreedyAlgorithm.greedyAlgorithm(data, data.noDays);
                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;
                    System.out.println("Finished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    solution.exportFile(input);

                    System.out.printf("\nBest score: %d points in %d milliseconds.", solution.getScore(), timeElapsed);

                    break;
                case "2":

                    System.out.println("Insert the minimum population size:");
                    input = scan.nextLine();
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

                    startTime = System.nanoTime();

                    System.out.println("Generating initial population...\n");

                    GeneticAlgorithm ga = new GeneticAlgorithm(procreationFactor, mutationRate, maxSteps, data, mutationType, minimumPopulationSize);

                    System.out.println("Population generated. Starting evolution...");

                    while (ga.getCurrentGeneration() < maxSteps) {
                        ga.algorithmStep();
                    }

                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;

                    System.out.println("Finished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    ga.getCurrentBestSolution().exportFile(input);

                    String result = String.format("\nBest score: %d points in %d milliseconds.", ga.getCurrentBestSolution().getScore(), timeElapsed);
                    System.out.println(result);
                    break;
                case "3":
                    System.out.printf("\n\nInsert the initial temperature:");
                    input = scan.nextLine();
                    double temperature = Double.parseDouble(input);

                    System.out.print("\nInsert the cooling factor:");
                    input = scan.nextLine();
                    double coolingFactor = Double.parseDouble(input);

                    startTime = System.nanoTime();
                    Solution solutionSA = SimulatedAnnealing.simulatedAnnealingAlgorithm(data, temperature, coolingFactor);
                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    solutionSA.exportFile(input);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionSA.getScore(), timeElapsed);
                    break;

                case "4":
                    System.out.printf("\n\nInsert the maximum number for the neighbor search:");
                    input = scan.nextLine();
                    int maxNeighbor = Integer.parseInt(input);

                    startTime = System.nanoTime();
                    Solution solutionHC = HillClimbing.hillClimbingAlgorithm(data, maxNeighbor);
                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    solutionHC.exportFile(input);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionHC.getScore(), timeElapsed);
                    break;
                case "5":
                    System.out.printf("\n\nInsert the desired neighborhood size:");
                    input = scan.nextLine();
                    int neighborhoodSize = Integer.parseInt(input);


                    startTime = System.nanoTime();
                    Solution solutionHCSA = HillClimbingSteepestAscent.hillClimbingSteepestAscentAlgorithm(data, neighborhoodSize);
                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    solutionHCSA.exportFile(input);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionHCSA.getScore(), timeElapsed);
                    break;
                case "6":
                    System.out.printf("\n\nInsert the desired neighborhood size:");
                    input = scan.nextLine();
                    int neighborhoodDesiredSize = Integer.parseInt(input);

                    startTime = System.nanoTime();
                    Solution solutionHCS = HillClimbingStochastic.hillClimbingStochasticAlgorithm(data, neighborhoodDesiredSize);
                    endTime = System.nanoTime();
                    timeElapsed = (endTime - startTime) / 1000000;
                    System.out.println("\nFinished execution.\n");

                    System.out.printf("\nInsert the solution file name:");
                    input = scan.nextLine();
                    solutionHCS.exportFile(input);
                    System.out.printf("\n\nBest score: %d points in %d milliseconds.", solutionHCS.getScore(), timeElapsed);
                    break;
                case "0":
                    System.out.printf("\n\nProgram terminated!");
                    break;
                default:
                    System.out.printf("\n\nPlease insert a valid option:");
                    break;
            }

        } while (!choice.equals("0"));
    }

    public static void printMenu() {
        System.out.printf("\n\n======================= MENU =======================\n\n");
        System.out.printf("1 - Non-Meta Heuristic Greedy Algorithm\n2 - Genetic Algorithm\n3 - Simulated Annealing Algorithm" +
                "\n4 - Hill Climbing Algorithm\n5 - Steepest-Ascent Hill Climbing Algorithm\n6 - Stochastic Hill Climbing Algorithm\n0 - Exit");
        System.out.printf("\n\nPlease select one option:");
    }
}
