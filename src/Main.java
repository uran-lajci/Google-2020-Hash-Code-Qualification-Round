import ga.GeneticAlgorithm;
import shared.*;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            printUsage();
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String algorithm = args[2];

        FileReader reader = new FileReader();
        Data data = reader.readFile(new File(inputPath));

        long startTime = System.nanoTime();
        Solution solution = null;

        long startTime_ga = System.currentTimeMillis();
        long maxDuration = 10 * 60 * 1000; // 10 minutes in milliseconds

        switch (algorithm) {
            case "1": // Greedy Algorithm
                if (args.length != 3) {
                    System.err.println("Usage for Greedy Algorithm: java Main <input> <output> 1");
                    System.exit(1);
                }
                solution = MainGreedyAlgorithm.greedyAlgorithm(data, data.noDays);
                break;

            case "2": // Genetic Algorithm
                if (args.length != 8) {
                    System.err.println("Usage for Genetic Algorithm: java Main <input> <output> 2 <minPopulation> <procreationFactor> <mutationRate> <maxGenerations> <mutationType>");
                    System.exit(1);
                }
                try {
                    int minPop = Integer.parseInt(args[3]);
                    double procreation = Double.parseDouble(args[4]);
                    double mutationRate = Double.parseDouble(args[5]);
                    int maxGen = Integer.parseInt(args[6]);
                    String mutationType = args[7];

                    GeneticAlgorithm ga = new GeneticAlgorithm(procreation, mutationRate, maxGen, data, mutationType, minPop);
                    while (ga.getCurrentGeneration() < maxGen) {
                        ga.algorithmStep();
                    }
                    solution = ga.getCurrentBestSolution();

                    while (
                            ga.getCurrentGeneration() < maxGen &&
                                    (System.currentTimeMillis() - startTime_ga) < maxDuration // Time check
                    ) {
                        ga.algorithmStep();
                    }
                    solution = ga.getCurrentBestSolution();
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter: " + e.getMessage());
                    System.exit(1);
                }
                break;

            case "3": // Simulated Annealing
                if (args.length != 5) {
                    System.err.println("Usage: java Main <input> <output> 3 <temperature> <coolingFactor>");
                    System.exit(1);
                }
                try {
                    double temp = Double.parseDouble(args[3]);
                    double cooling = Double.parseDouble(args[4]);

                    // Run SA with time constraint
                    solution = SimulatedAnnealing.simulatedAnnealingAlgorithm(
                            data, temp, cooling, maxDuration, startTime_ga
                    );
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter: " + e.getMessage());
                    System.exit(1);
                }
                break;

            case "4": // Hill Climbing
                if (args.length != 4) {
                    System.err.println("Usage for Hill Climbing: java Main <input> <output> 4 <maxNeighbor>");
                    System.exit(1);
                }
                try {
                    int maxNeighbor = Integer.parseInt(args[3]);
                    solution = HillClimbing.hillClimbingAlgorithm(
                            data, maxNeighbor, maxDuration, startTime_ga
                    );
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter: " + e.getMessage());
                    System.exit(1);
                }
                break;

            case "5": // Steepest-Ascent Hill Climbing
                 if (args.length != 4) {
                    System.err.println("Usage: java Main <input> <output> 5 <neighborhoodSize>");
                    System.exit(1);
                }
                try {
                    int neighborhoodSize = Integer.parseInt(args[3]);

                    solution = HillClimbingSteepestAscent.hillClimbingSteepestAscentAlgorithm(
                        data, neighborhoodSize, maxDuration, startTime_ga
                    );
                } catch (NumberFormatException e) catch (NumberFormatException e) {
                    System.err.println("Invalid parameter: " + e.getMessage());
                    System.exit(1);
                }
                break;

            case "6": // Stochastic Hill Climbing
                if (args.length != 4) {
                    System.err.println("Usage for Stochastic HC: java Main <input> <output> 6 <neighborhoodSize>");
                    System.exit(1);
                }
                try {
                    int neighborhoodSize = Integer.parseInt(args[3]);
                    solution = HillClimbingStochastic.hillClimbingStochasticAlgorithm(
                        data, neighborhoodSize, maxDuration, startTime_ga
                    );
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter: " + e.getMessage());
                    System.exit(1);
                }
                break;

            default:
                System.err.println("Invalid algorithm choice. Valid options are 1-6.");
                System.exit(1);
        }

        long endTime = System.nanoTime();
        long timeElapsed = (endTime - startTime) / 1_000_000;

        solution.exportFile(outputPath);
        System.out.printf("Best score: %d points in %d milliseconds.%n", solution.getScore(), timeElapsed);
    }

    private static void printUsage() {
        System.out.println("Usage: java Main <inputFile> <outputFile> <algorithm> [parameters...]");
        System.out.println("Algorithms and their parameters:");
        System.out.println("1 - Greedy Algorithm: No additional parameters");
        System.out.println("2 - Genetic Algorithm: <minPopulation> <procreationFactor> <mutationRate> <maxGenerations> <mutationType>");
        System.out.println("3 - Simulated Annealing: <temperature> <coolingFactor>");
        System.out.println("4 - Hill Climbing: <maxNeighbor>");
        System.out.println("5 - Steepest-Ascent Hill Climbing: <neighborhoodSize>");
        System.out.println("6 - Stochastic Hill Climbing: <neighborhoodSize>");
    }
}