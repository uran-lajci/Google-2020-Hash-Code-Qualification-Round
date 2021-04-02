package shared;

import java.util.*;

public class SimulatedAnnealing {


    public static Solution simulatedAnnealingAlgorithm(Data data, double temperature, double coolingFactor) {
        Data currentData = new Data(data);
        Solution currentSolution = new Solution(currentData.noDays);

        List<Library> ignoredLibraries = new ArrayList<>();

        // Randomize the libraries list
        Collections.shuffle(currentData.libraries);

        int libraryCount = 0;


        while (libraryCount < currentData.libraries.size() && currentSolution.getSignUpTime() + currentData.libraries.get(libraryCount).signUpTime < currentData.noDays) {
            Library l = currentData.libraries.get(libraryCount);
            currentSolution.addLibrary(new Library(l));
            libraryCount++;
        }

        while (libraryCount < currentData.libraries.size()) {
            ignoredLibraries.add(new Library(currentData.libraries.get(libraryCount)));
            libraryCount++;
        }

        Solution bestSolution = new Solution(currentSolution);

        for (double t = temperature; t > 1; t *= coolingFactor) {
            Solution neighbor = new Solution(currentSolution);
            boolean validSolution = false;

            int i = 0;
            while (!validSolution) {

                int index1, index2;
                Library lb1, lb2;


                index1 = (int) (currentSolution.getNoLibraries() * Math.random());
                lb1 = currentSolution.getLibraries().get(index1);

                if (ignoredLibraries.isEmpty()) {
                    index2 = (int) (currentSolution.getNoLibraries() * Math.random());
                    neighbor.setNewLibrary(index2, lb1);
                    lb2 = currentSolution.getLibraries().get(index2);
                } else {
                    index2 = (int) (ignoredLibraries.size() * Math.random());
                    lb2 = ignoredLibraries.get(index2);
                    ignoredLibraries.add(index2, lb1);
                }

                neighbor.setNewLibrary(index1, lb2);


                if (neighbor.isSolutionValid() || i > 100) {
                    validSolution = true;
                } else {
                    neighbor.setNewLibrary(index1, lb1);

                    if (ignoredLibraries.isEmpty()) {
                        neighbor.setNewLibrary(index2, lb2);
                    } else {
                        ignoredLibraries.add(index2, lb2);
                    }
                    i++;
                }
            }

            if (i != 100) {
                int currentScore = currentSolution.updateScore();
                int neighbourScore = neighbor.updateScore();

                if (Math.random() < calculateProbability(currentScore, neighbourScore, t)) {
                    currentSolution = new Solution(neighbor);
                }

                if (currentSolution.getScore() > bestSolution.getScore()) {
                    bestSolution = new Solution(currentSolution);
                }
            }
        }

        bestSolution.updateScore();
        return bestSolution;
    }

    public static double calculateProbability(double score1, double score2, double temperature) {
        if (score2 > score1) {
            return 1;
        }
        return Math.exp((score2-score1) / temperature);
    }

}
