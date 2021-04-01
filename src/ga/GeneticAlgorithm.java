package ga;

import shared.Data;
import shared.Library;
import shared.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;

public class GeneticAlgorithm {

    private double procreationFactor = 2;

    private double mutationRate = 0.03;

    private int populationSize;

    private int maxSteps;

    private int nextGenerationSize;

    private Solution currentBestSolution;

    private String mutationType;

    private int minimumPopulationSize;

    private Data data;

    private int currentGeneration;

    private List<Solution> activePopulation;
    private List<Solution> nextGeneration;

    public GeneticAlgorithm(double procreationFactor, double mutationRate, int maxSteps, Data data, String mutationType, int minimumPopulationSize) {
        this.maxSteps = maxSteps;
        this.procreationFactor = procreationFactor;
        this.mutationRate = mutationRate;
        this.data = data;
        this.mutationType = mutationType;
        this.currentGeneration = 0;
        this.minimumPopulationSize = minimumPopulationSize;

        this.activePopulation = new ArrayList<>();
        this.currentBestSolution = new Solution(data.noDays);

        generateInitialPopulation();

    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public Solution getCurrentBestSolution() {
        return currentBestSolution;
    }

    private void findAndSaveBestSolution() {
        Solution bestInPopulation;
        if (activePopulation.size() == 0) {
            bestInPopulation = new Solution(data.noDays);
        } else {
            bestInPopulation = activePopulation.get(0);
        }
        for (Solution individual : activePopulation) {
            if (individual.getScore() > bestInPopulation.getScore()) {
                bestInPopulation = individual;
            }
        }
        String res = String.format("Generation %d best score: %d points", this.getCurrentGeneration(), bestInPopulation.getScore());
        System.out.println(res);
        if (this.currentBestSolution.getScore() < bestInPopulation.getScore()) {
            this.currentBestSolution = bestInPopulation;
        }
    }

    public void algorithmStep() {

        reproduction();

        makeNewPopulation();

        findAndSaveBestSolution();

        this.currentGeneration++;
    }

    private void generateInitialPopulation() {
        int days = this.data.noDays;
        int index = 0;
        Solution currentSolution = new Solution(days);
        List<Library> auxList = new ArrayList<>(this.data.libraries);
        Collections.shuffle(auxList);
        while (activePopulation.size() < this.minimumPopulationSize) {
            if (index == auxList.size()) {
                if (currentSolution.getNoLibraries() == auxList.size()) {
                    currentSolution.updateScore();
                    this.activePopulation.add(new Solution(currentSolution));
                    currentSolution = new Solution(days);
                }
                Collections.shuffle(auxList);
                index = 0;
            }

            if (currentSolution.getSignUpTime() + auxList.get(index).signUpTime > days) {
                currentSolution.updateScore();
                this.activePopulation.add(new Solution(currentSolution));
                currentSolution = new Solution(days);
            }
            currentSolution.addLibrary(auxList.get(index));
            index++;
        }
        this.populationSize = this.activePopulation.size();
        this.nextGenerationSize = (int) (procreationFactor * this.populationSize);
    }

    private void reproduction() {

        nextGeneration = new ArrayList<>();

        while (nextGeneration.size() < nextGenerationSize) {

            Solution parent1 = selection();
            Solution parent2 = selection();

            Solution[] children = crossover(parent1, parent2);

            mutation(children[0]);
            mutation(children[1]);

            nextGeneration.add(children[0]);
            nextGeneration.add(children[1]);
        }
    }

    private void makeNewPopulation() {

        for (Solution individual : nextGeneration) {
            if (!activePopulation.contains(individual)) {
                activePopulation.add(individual);
            }
        }
        activePopulation.sort(Collections.reverseOrder());
        activePopulation = new ArrayList<>(activePopulation.subList(0, populationSize));
    }

    private Solution selection() {

        SplittableRandom random = new SplittableRandom();

        Solution first = activePopulation.get(random.nextInt(populationSize));
        Solution second = activePopulation.get(random.nextInt(populationSize));

        if (first.getScore() > second.getScore()) {
            return first;
        } else {
            return second;
        }
    }

    private Solution[] crossover(Solution parent1, Solution parent2) {
        SplittableRandom random = new SplittableRandom();

        int minimumParentSize;
        Solution[] children = new Solution[2];

        List<Library> child1 = new ArrayList<>();
        List<Library> child2 = new ArrayList<>();

        if (parent1.getNoLibraries() < parent2.getNoLibraries()) {
            minimumParentSize = parent1.getNoLibraries();
        } else {
            minimumParentSize = parent2.getNoLibraries();
        }

        int crossoverPoint = random.nextInt(minimumParentSize);

        children[0] = new Solution(this.data.noDays);
        children[1] = new Solution(this.data.noDays);

        List<Library> parent1Libraries = parent1.getLibraries();
        List<Library> parent2Libraries = parent1.getLibraries();

        for (int i = 0; i < crossoverPoint; i++) {
            children[0].addLibrary(parent1Libraries.get(i));
            children[1].addLibrary(parent2Libraries.get(i));
        }
        for (int i = crossoverPoint; i < minimumParentSize; i++) {
            children[0].addLibrary(parent2Libraries.get(i));
            children[1].addLibrary(parent1Libraries.get(i));
        }

        children[0].cleanLibraries();
        children[1].cleanLibraries();

        return children;
    }

    private Solution mutation(Solution individual) {

        SplittableRandom random = new SplittableRandom();

        for (int i = 0; i < individual.getNoLibraries(); i++) {
            double rn = random.nextDouble(1);
            if (rn < mutationRate) {
                //if swap, swap current library with a random one from the list of all libraries
                if (mutationType.equalsIgnoreCase("switch") || individual.getNoLibraries() == 1) {
                    int newLibraryPosition = random.nextInt(this.data.libraries.size());
                    Library newLibrary = this.data.libraries.get(newLibraryPosition);
                    individual.getLibraries().set(i, newLibrary);
                    individual.cleanLibraries();
                } else {
                    if (i == individual.getNoLibraries() - 1) {
                        Collections.swap(individual.getLibraries(), i, i - 1);
                    } else {
                        Collections.swap(individual.getLibraries(), i, i + 1);
                    }
                }
            }
        }
        individual.updateScore();
        return individual;
    }

}
