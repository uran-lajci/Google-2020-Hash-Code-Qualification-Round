package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HillClimbingStochastic {

    public static Solution hillClimbingStochasticAlgorithm(Data data, int neighborhoodSize) {

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

        // Add possible ignored libraries that still fit the schedule
        for (int libraryCounter = 0; libraryCounter < ignoredLibraries.size(); libraryCounter++) {
            Library l = ignoredLibraries.get(libraryCounter);
            if (currentSolution.getSignUpTime() + l.signUpTime < currentData.noDays) {
                currentSolution.addLibrary(new Library(l));
                ignoredLibraries.remove(libraryCounter);
            }
        }


        boolean betterNeighborFound = true;

        // Do the algorithm until there isn't a better neighborhood
        while (betterNeighborFound) {

            // List to store the neighborhood found
            List<Solution> listOfNeighbor = new ArrayList<>();

            // Count the found neighbours
            int neighborCount = 0;

            // Update the score to ensure it is up to date when compared with others
            int currentScore = currentSolution.updateScore();

            // Until the number of neighbors found is equal to the desired one
            while (neighborCount < neighborhoodSize) {

                // Start the neighbor from the current solution
                Solution neighbor = new Solution(currentSolution);
                boolean validSolution = false;

                int i = 0;

                // Do this until we get a valid neighbor
                // If a solution has more throughput than the allowed than it is not a valid neighbor
                while (!validSolution) {

                    // Indexes to switch libraries
                    int index1, index2;
                    Library lb1, lb2, lb1Copy, lb2Copy;

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

                // If the neighbor as a better score than the current solution
                // Register the new neighbor in the list and increment the neighbors count
                neighbor.updateScore();

                if (neighbor.getScore() > currentScore) {
                    listOfNeighbor.add(neighbor);
                }

                neighborCount++;
            }

            if (!listOfNeighbor.isEmpty()) {
                int chosenIndex = (int) (listOfNeighbor.size() * Math.random());
                // The neighbor with the chosenIndex will substitute the current solution
                currentSolution = new Solution(listOfNeighbor.get(chosenIndex));
            } else {
                // If there are no neighbors with better score the algorithm will end
                betterNeighborFound = false;
            }
        }

        // Update the score before returning the solution
        currentSolution.updateScore();
        return currentSolution;
    }

}
