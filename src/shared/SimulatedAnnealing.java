package shared;

import java.util.*;

public class SimulatedAnnealing {


    public static Solution simulatedAnnealingAlgorithm(
            Data data,
            double temperature,
            double coolingFactor,
            long maxDuration,
            long startTime
    ) {

        // Create a copy of the data so the original doesn't get destroyed and can be used for other executions
        Data currentData = new Data(data);

        // Create a Solution object to start choosing the libraries
        Solution currentSolution = new Solution(currentData.noDays);

        // Array to store the libraries ignored in the first solution
        List<Library> ignoredLibraries = new ArrayList<>();

        // Randomize the libraries list
        Collections.shuffle(currentData.libraries);

        int libraryCount = 0;


        // Do this while there are available libraries to signup or until the maximum number of days is reached
        while (libraryCount < currentData.libraries.size() && currentSolution.getSignUpTime() + currentData.libraries.get(libraryCount).signUpTime < currentData.noDays) {
            Library l = currentData.libraries.get(libraryCount);
            currentSolution.addLibrary(new Library(l));
            libraryCount++;
        }

        // Add the remaining libraries to a list of ignored ones
        while (libraryCount < currentData.libraries.size()) {
            ignoredLibraries.add(new Library(currentData.libraries.get(libraryCount)));
            libraryCount++;
        }

        // Clone the first solution since in the beginning it also is the best one
        Solution bestSolution = new Solution(currentSolution);

        currentSolution.updateScore();
        System.out.println("Initial solution's score: " + currentSolution.getScore() + " points.");

        // Until the temperature reaches one
        for (double t = temperature; t > 1 && (System.currentTimeMillis() - startTime) < maxDuration; t *= coolingFactor) {

            // Start the neighbor from the current solution
            Solution neighbor = new Solution(currentSolution);
            boolean validSolution = false;

            int i = 0;

            // Do this until we get a valid neighbor
            // If a solution has more throughput than the allowed than it is not a valid neighbor
            while (!validSolution) {

                // Indexes to switch libraries
                int index1, index2;
                Library lb1, lb2,lb1Copy,lb2Copy;

                // First index is from the current solution
                index1 = (int) (currentSolution.getNoLibraries() * Math.random());
                lb1 = currentSolution.getLibraries().get(index1);

                lb1Copy = new Library(lb1);

                // Second index is from the ignored list if it not empty
                // Or from the current solution if the ignored list is empty
                if (ignoredLibraries.isEmpty()) {
                    index2 = (int) (currentSolution.getNoLibraries() * Math.random());
                    neighbor.setNewLibrary(index2, lb1);
                    lb2 = currentSolution.getLibraries().get(index2);
                } else {
                    index2 = (int) (ignoredLibraries.size() * Math.random());
                    lb2 = ignoredLibraries.get(index2);
                    ignoredLibraries.add(index2, lb1);
                }

                lb2Copy = new Library(lb2);

                // Switch the libraries
                neighbor.setNewLibrary(index1, lb2Copy);


                // If the solution is valid or its the 100 try the cycle stops
                if (neighbor.isSolutionValid() || i > 100) {
                    validSolution = true;
                } else {

                    // If the solution is not valid changes must be reverted
                    neighbor.setNewLibrary(index1, lb1Copy);

                    if (ignoredLibraries.isEmpty()) {
                        neighbor.setNewLibrary(index2, lb2);
                    } else {
                        ignoredLibraries.add(index2, lb2);
                    }

                    // Counter incremented to ensure we dont get an infinite loop
                    i++;
                }
            }

            // If the new neighbor is valid
            if (i != 100) {

                //Calculate the score for the current algorithm
                int currentScore = currentSolution.updateScore();

                // Calculate the score for the neighbor one
                int neighbourScore = neighbor.updateScore();

                // Calculate the probability for the neighbor to become the current solution
                if (Math.random() < calculateProbability(currentScore, neighbourScore, t)) {
                    currentSolution = new Solution(neighbor);
                }

                // If the current is better solution is better than save it as the best one for now
                if (currentSolution.getScore() > bestSolution.getScore()) {
                    bestSolution = new Solution(currentSolution);
                }
            }

//            System.out.println("\nTemperature: " + t);
//            System.out.println("Current score: " + currentSolution.getScore() +  " points.");
//            System.out.println("Best score: " + bestSolution.getScore() +  " points.");

        }

        // Update the score before returning the solution
        bestSolution.updateScore();
        return bestSolution;
    }

    public static double calculateProbability(double score1, double score2, double temperature) {
        // If the neighbor score is best return 1 to ensure that neighbor is chosen
        if (score2 > score1) {
            return 1;
        }

        // If not calculate a probability
        return 1/(1 + Math.exp((score2-score1) / temperature));
    }

}
