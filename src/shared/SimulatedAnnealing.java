package shared;

import java.util.*;

public class SimulatedAnnealing {

    private static double temperature = 1000000000;
    private static double coolingFactor = 0.990;


    public static Solution simulatedAnnealingAlgorithm(Data data) {
        Data currentData = data.copyClass();
        Solution currentSolution = new Solution(currentData.noDays);

        List<Library> ignoredLibraries = new ArrayList<>();

        // Randomize the libraries list
        Collections.shuffle(currentData.libraries);

        int libraryCount = 0;


        while (libraryCount < currentData.libraries.size() && currentSolution.getSignUpTime() + currentData.libraries.get(libraryCount).signUpTime < currentData.noDays) {
            Library l = currentData.libraries.get(libraryCount);
            currentSolution.addLibrary(l);
            libraryCount++;
        }

        while (libraryCount < currentData.libraries.size()) {
            ignoredLibraries.add(currentData.libraries.get(libraryCount));
            libraryCount++;
        }

        Solution bestSolution = new Solution(currentSolution);

        for (double t = temperature; t > 1; t *= coolingFactor) {
            Solution neighbor = new Solution(currentSolution);
            Boolean validSolution = false;

            int i = 0;
            while (!validSolution) {
                int index1 = (int) (currentSolution.getNoLibraries() * Math.random());
                int index2 = (int) (ignoredLibraries.size() * Math.random());

                Library lb1 = currentSolution.getLibraries().get(index1);
                Library lb2 = ignoredLibraries.get(index2);

                neighbor.setNewLibrary(index1, lb2);
                ignoredLibraries.add(index2, lb1);

                if (neighbor.isSolutionValid() || i > 100) {
                    validSolution = true;
                } else {
                    neighbor.setNewLibrary(index1, lb1);
                    ignoredLibraries.add(index2, lb2);
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

        return bestSolution;
    }

    public static double calculateProbability(double score1, double score2, double temperature) {
        if (score2 > score1) {
            return 1;
        }
        return Math.exp((score1 - score2) / temperature);
    }

}
