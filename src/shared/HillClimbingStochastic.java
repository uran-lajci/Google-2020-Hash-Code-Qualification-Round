package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HillClimbingStochastic {

    public static Solution hillClimbingStochasticAlgorithm(
        Data data,
        int neighborhoodSize,
        long maxDuration,
        long startTime
    ) {
        Data currentData = new Data(data);
        Solution currentSolution = new Solution(currentData.noDays);
        List<Library> ignoredLibraries = new ArrayList<>();
        Collections.shuffle(currentData.libraries);

        // Initialize solution with valid libraries
        int libraryCount = 0;
        while (libraryCount < currentData.libraries.size() &&
               currentSolution.getSignUpTime() + currentData.libraries.get(libraryCount).signUpTime < currentData.noDays) {
            Library l = currentData.libraries.get(libraryCount);
            currentSolution.addLibrary(new Library(l));
            libraryCount++;
        }

        // Add remaining libraries to ignored list
        while (libraryCount < currentData.libraries.size()) {
            ignoredLibraries.add(new Library(currentData.libraries.get(libraryCount)));
            libraryCount++;
        }

        // Add valid ignored libraries
        for (int i = 0; i < ignoredLibraries.size(); i++) {
            Library l = ignoredLibraries.get(i);
            if (currentSolution.getSignUpTime() + l.signUpTime < currentData.noDays) {
                currentSolution.addLibrary(new Library(l));
                ignoredLibraries.remove(i);
                i--; // Adjust index after removal
            }
        }

        currentSolution.updateScore();
        Solution bestSolution = new Solution(currentSolution); // Track best solution
        System.out.println("Initial score: " + bestSolution.getScore());

        boolean betterNeighborFound = true;

        // Time-limited loop
        while (betterNeighborFound && (System.currentTimeMillis() - startTime) < maxDuration) {
            List<Solution> improvingNeighbors = new ArrayList<>();
            int neighborCount = 0;

            // Generate neighbors within time limit
            while (neighborCount < neighborhoodSize &&
                   (System.currentTimeMillis() - startTime) < maxDuration) {

                Solution neighbor = new Solution(currentSolution);
                boolean validSolution = false;
                int attempts = 0;

                // Time-aware neighbor generation
                while (!validSolution && attempts <= 100 &&
                       (System.currentTimeMillis() - startTime) < maxDuration) {

                    int index1 = (int) (currentSolution.getNoLibraries() * Math.random());
                    Library lb1 = currentSolution.getLibraries().get(index1);
                    Library lb2;

                    if (ignoredLibraries.isEmpty()) {
                        int index2 = (int) (currentSolution.getNoLibraries() * Math.random());
                        lb2 = currentSolution.getLibraries().get(index2);
                    } else {
                        int index2 = (int) (ignoredLibraries.size() * Math.random());
                        lb2 = ignoredLibraries.get(index2);
                    }

                    // Swap libraries
                    neighbor.setNewLibrary(index1, new Library(lb2));
                    validSolution = neighbor.isSolutionValid();
                    attempts++;
                }

                if (validSolution) {
                    neighbor.updateScore();
                    if (neighbor.getScore() > currentSolution.getScore()) {
                        improvingNeighbors.add(neighbor);
                    }
                    neighborCount++;
                }
            }

            // Select a random improving neighbor
            if (!improvingNeighbors.isEmpty()) {
                int chosenIndex = (int) (Math.random() * improvingNeighbors.size());
                currentSolution = new Solution(improvingNeighbors.get(chosenIndex));
                if (currentSolution.getScore() > bestSolution.getScore()) {
                    bestSolution = new Solution(currentSolution);
                }
            } else {
                betterNeighborFound = false;
            }
        }

        bestSolution.updateScore();
        return bestSolution;
    }
}