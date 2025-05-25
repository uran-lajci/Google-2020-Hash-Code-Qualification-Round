package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HillClimbingSteepestAscent {

    public static Solution hillClimbingSteepestAscentAlgorithm(
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

        List<Solution> neighbors = new ArrayList<>();
        boolean betterNeighborFound = true;

        // Time-limited loop
        while (betterNeighborFound && (System.currentTimeMillis() - startTime) < maxDuration) {
            neighbors.clear();
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
                    neighbors.add(neighbor);
                    neighborCount++;
                }
            }

            // Evaluate neighbors
            int currentScore = currentSolution.getScore();
            int bestNeighborScore = currentScore;
            Solution bestNeighbor = null;

            for (Solution s : neighbors) {
                if (s.getScore() > bestNeighborScore) {
                    bestNeighborScore = s.getScore();
                    bestNeighbor = s;
                }
            }

            // Update solutions
            if (bestNeighbor != null) {
                currentSolution = new Solution(bestNeighbor);
                if (bestNeighborScore > bestSolution.getScore()) {
                    bestSolution = new Solution(bestNeighbor);
                }
            } else {
                betterNeighborFound = false;
            }
        }

        bestSolution.updateScore();
        return bestSolution;
    }
}