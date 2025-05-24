package ga;

import shared.Data;
import shared.Library;
import shared.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;

public class GeneticAlgorithm {

    /**
     * Determines the number of individuals in the next generation
     */
    private double procreationFactor = 2;

    /**
     * Chance of mutation
     */
    private double mutationRate = 0.03;

    /**
     * Size of the population
     */
    private int populationSize;

    /**
     * Maximum number of generations
     */
    private int maxSteps;

    /**
     * Size of the next generation
     */
    private int nextGenerationSize;

    /**
     * Best solution found
     */
    private Solution currentBestSolution;

    /**
     * Type of mutation
     */
    private String mutationType;

    /**
     * Minimum size of the population
     */
    private int minimumPopulationSize;

    /**
     * Data with all libraries and books
     */
    private Data data;

    /**
     * Number of the current generation
     */
    private int currentGeneration;

    /**
     * Current generation's population
     */
    private List<Solution> activePopulation;

    /**
     * Next generation's offspring
     */
    private List<Solution> nextGeneration;

    /**
     *
     * @param procreationFactor determines next generation's size
     * @param mutationRate chance of mutation
     * @param maxSteps maximum number of generations
     * @param data libraries and books
     * @param mutationType type of mutation
     * @param minimumPopulationSize minimum size of the population
     */
    public GeneticAlgorithm(double procreationFactor, double mutationRate, int maxSteps, Data data, String mutationType, int minimumPopulationSize) {
        this.maxSteps = maxSteps;
        this.procreationFactor = procreationFactor;
        this.mutationRate = mutationRate;
        this.data = new Data(data);
        this.mutationType = mutationType;
        this.currentGeneration = 0;
        this.minimumPopulationSize = minimumPopulationSize;

        this.activePopulation = new ArrayList<>();
        this.currentBestSolution = new Solution(this.data.noDays);

        //generate initial solutions
        generateInitialPopulation();

    }

    /**
     * Get the current generation
     * @return current generation number
     */
    public int getCurrentGeneration() {
        return currentGeneration;
    }

    /**
     * Get the best solution
     * @return best solution so far
     */
    public Solution getCurrentBestSolution() {
        // update the current solution's chosen books and score
        currentBestSolution.updateScore();
        return currentBestSolution;
    }

    /**
     * find the best solution in the population and compare it to the best so far
     */
    private void findAndSaveBestSolution() {

        Solution bestInPopulation;
        //check if population is empty
        if (activePopulation.size() == 0) {
            bestInPopulation = new Solution(data.noDays);
        } else {
            bestInPopulation = activePopulation.get(0);
        }

        //find the best in population
        for (Solution individual : activePopulation) {
            if (individual.getScore() > bestInPopulation.getScore()) {
                bestInPopulation = individual;
            }
        }

        // compare solutions
        if (this.currentBestSolution.getScore() < bestInPopulation.getScore()) {
            this.currentBestSolution = bestInPopulation;
        }
    }

    /**
     * Advance generation
     */
    public void algorithmStep() {

        // simulate reproduction
        reproduction();

        //decide new population
        makeNewPopulation();

        // find best solution
        findAndSaveBestSolution();

        //increment generation number
        this.currentGeneration++;

        System.out.println("Generation " + this.currentGeneration + " best score: " + this.currentBestSolution.getScore() + " points.");
    }

    private void generateInitialPopulation() {
        int days = this.data.noDays;
        int index = 0;
        Solution currentSolution = new Solution(days);

        // create auxiliary list
        List<Library> auxList = new ArrayList<>(this.data.libraries);
        //shuffle the list
        Collections.shuffle(auxList);

        // do while population size minimum isn't met
        while (activePopulation.size() < this.minimumPopulationSize) {
            // if library is last on the list
            if (index == auxList.size()) {
                // if current solution's number of libraries is the same as the number of existing libraries, add the solution to the active population and create a new one
                if (currentSolution.getNoLibraries() == auxList.size()) {
                    currentSolution.updateScore();
                    this.activePopulation.add(new Solution(currentSolution));
                    currentSolution = new Solution(days);
                }
                Collections.shuffle(auxList);
                index = 0;
            }

            // if sign up time limit is reached add solution to the active population
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

    /**
     * simulate reproduction and mutation
     */
    private void reproduction() {

        nextGeneration = new ArrayList<>();

        // do while next generation's size isn't reached
        while (nextGeneration.size() < nextGenerationSize) {

            // select parents
            Solution parent1 = selection();
            Solution parent2 = selection();

            // create offspring
            Solution[] children = crossover(parent1, parent2);

            // apply mutation
            mutation(children[0]);
            mutation(children[1]);

            // add offspring to next generation
            nextGeneration.add(children[0]);
            nextGeneration.add(children[1]);
        }
    }

    /**
     * Select next generation
     */
    private void makeNewPopulation() {

        // check for duplicates
        for (Solution individual : nextGeneration) {
            if (!activePopulation.contains(individual)) {
                activePopulation.add(individual);
            }
        }

        // sort individuals by score (descending)
        activePopulation.sort(Collections.reverseOrder());
        // select best
        activePopulation = new ArrayList<>(activePopulation.subList(0, populationSize));
    }

    /**
     * Select random individual
     * @return parent selected
     */
    private Solution selection() {

        SplittableRandom random = new SplittableRandom();

        // choose random individuals
        Solution first = activePopulation.get(random.nextInt(populationSize));
        Solution second = activePopulation.get(random.nextInt(populationSize));

        // select fittest
        if (first.getScore() > second.getScore()) {
            return first;
        } else {
            return second;
        }
    }

    /**
     * Crossover parents (single point)
     * @param parent1 first parent
     * @param parent2 second parent
     * @return offspring
     */
    private Solution[] crossover(Solution parent1, Solution parent2) {
        SplittableRandom random = new SplittableRandom();

        int minimumParentSize;
        Solution[] children = new Solution[2];

        // check smallest parent
        if (parent1.getNoLibraries() < parent2.getNoLibraries()) {
            minimumParentSize = parent1.getNoLibraries();
        } else {
            minimumParentSize = parent2.getNoLibraries();
        }

        // select random crossover point
        int crossoverPoint = random.nextInt(minimumParentSize);

        children[0] = new Solution(this.data.noDays);
        children[1] = new Solution(this.data.noDays);

        List<Library> parent1Libraries = parent1.getLibraries();
//        List<Library> parent2Libraries = parent1.getLibraries(); // Incorrect
        List<Library> parent2Libraries = parent2.getLibraries(); // Correct

        // generate first part
        for (int i = 0; i < crossoverPoint; i++) {
            children[0].addLibrary(parent1Libraries.get(i));
            children[1].addLibrary(parent2Libraries.get(i));
        }

        // generate second part
        for (int i = crossoverPoint; i < parent2Libraries.size(); i++) {
            children[0].addLibrary(parent2Libraries.get(i));
        }

        for (int i = crossoverPoint; i < parent1Libraries.size(); i++) {
            children[1].addLibrary(parent1Libraries.get(i));
        }

        // remove duplicates and invalid solutions
        children[0].cleanLibraries();
        children[1].cleanLibraries();

        return children;
    }

    /**
     * Apply mutation
     * @param individual individual to mutate
     * @return mutated individual
     */
    private Solution mutation(Solution individual) {

        SplittableRandom random = new SplittableRandom();

        for (int i = 0; i < individual.getNoLibraries(); i++) {
            double rn = random.nextDouble(1);

            // if library mutates
            if (rn < mutationRate) {
                //if swap, swap current library with a random one from the list of all libraries
                if (mutationType.equalsIgnoreCase("switch") || individual.getNoLibraries() == 1) {
                    int newLibraryPosition = random.nextInt(this.data.libraries.size());
                    Library newLibrary = this.data.libraries.get(newLibraryPosition);
                    individual.setNewLibrary(i, newLibrary);
                    individual.cleanLibraries();
                } else { // else swap positions between libraries
                    if (i == individual.getNoLibraries() - 1) {
                        Collections.swap(individual.getLibraries(), i, i - 1);
                    } else {
                        Collections.swap(individual.getLibraries(), i, i + 1);
                    }
                }
            }
        }

        // update the individual's score
        individual.updateScore();

        return individual;
    }

}
