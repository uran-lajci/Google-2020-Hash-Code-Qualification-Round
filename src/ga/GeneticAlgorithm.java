package ga;

import shared.Data;
import shared.Library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;

public class GeneticAlgorithm {

    private List<Library> libraries;

    private double procreationFactor = 2;

    private double mutationRate = 0.03;

    private int populationSize;

    private int maxSteps;

    private int nextGenerationSize;

    private Gene currentBestSolution;

    private String mutationType;

    private Data data;

    private List<Gene> activePopulation;
    private List<Gene> nextGeneration;

    public GeneticAlgorithm(double procreationFactor, double mutationRate, int maxSteps, Data data, String mutationType) {
        this.maxSteps = maxSteps;
        this.procreationFactor = procreationFactor;
        this.mutationRate = mutationRate;
        this.data = data;
        this.libraries = data.libraries;
        this.mutationType = mutationType;

        activePopulation = new ArrayList<>();
        currentBestSolution = new Gene(data.noDays);

        generateInitialPopulation();

        findAndSaveBestSolution();
    }

    private void findAndSaveBestSolution() {

        Gene bestInPopulation = activePopulation.get(0);
        for (Gene individual : activePopulation) {
            if (individual.compareTo(bestInPopulation) < 0) {
                bestInPopulation = individual;
            }
        }

        if (currentBestSolution.compareTo(bestInPopulation) > 0) {
            currentBestSolution = bestInPopulation;
        }
    }

    public void algorithmStep() {

        reproduction();

        makeNewPopulation();

        findAndSaveBestSolution();

    }

    private void generateInitialPopulation() {
        int days = this.data.noDays;

        Collections.shuffle(this.libraries);

        List<Library> auxLibraryList = new ArrayList<>(this.libraries);

        Gene currentGene = new Gene(this.data.noDays);

        for (int i = 0; i < this.libraries.size(); i++) {

            if (currentGene.getSignupTime() + auxLibraryList.get(0).signUpTime > days){
                this.activePopulation.add(new Gene(currentGene));
                currentGene = new Gene(days);
            }else{
                currentGene.addLibrary(auxLibraryList.remove(0));
            }
        }
        this.populationSize = this.activePopulation.size();
        this.nextGenerationSize = (int) (procreationFactor * this.populationSize);
    }

    private void reproduction() {

        nextGeneration = new ArrayList<>(nextGenerationSize);

        while (nextGeneration.size() < nextGenerationSize) {

            Gene parent1 = selection();
            Gene parent2 = selection();

            Gene[] children = crossover(parent1, parent2);

            mutation(children[0]);
            mutation(children[1]);

            nextGeneration.add(children[0]);
            nextGeneration.add(children[1]);
        }
    }

    private void makeNewPopulation() {

        activePopulation.addAll(nextGeneration);
        Collections.sort(activePopulation);
        activePopulation = new ArrayList<>(activePopulation.subList(0, populationSize));
    }

    private Gene selection() {

        SplittableRandom random = new SplittableRandom();

        Gene first = activePopulation.get(random.nextInt(populationSize));
        Gene second = activePopulation.get(random.nextInt(populationSize));

        if (first.calculateScore() > second.calculateScore())
            return first;
        else
            return second;
    }

    private Gene[] crossover(Gene parent1, Gene parent2) {

        SplittableRandom random = new SplittableRandom();

        int minimumParentSize;

        Gene[] children = new Gene[2];

        List<Library> child1 = new ArrayList<>();
        List<Library> child2 = new ArrayList<>();

        if (parent1.getNoLibraries() < parent2.getNoLibraries()){
            minimumParentSize = parent1.getNoLibraries();
        }else{
            minimumParentSize = parent2.getNoLibraries();
        }

        int crossoverPoint = random.nextInt(minimumParentSize);

        List<Library> parent1Libraries = parent1.getLibraries();
        List<Library> parent2Libraries = parent1.getLibraries();

        for (int i = 0; i < crossoverPoint; i++) {
            child1.add(i,parent1Libraries.get(i));
            child2.add(i,parent2Libraries.get(i));
        }
        for (int i = crossoverPoint; i < minimumParentSize; i++) {
            child1.add(i,parent1Libraries.get(i));
            child2.add(i,parent2Libraries.get(i));
        }

        children[0] = new Gene(child1, this.data.noDays);
        children[1] = new Gene(child2, this.data.noDays);

        children[0].cleanLibraries();
        children[1].cleanLibraries();

        return children;
    }

    private Gene mutation(Gene individual) {

        SplittableRandom random = new SplittableRandom();

        for (int i = 0; i < individual.getNoLibraries(); i++) {
            if (random.nextDouble() < mutationRate) {
                //if swap, swap current library with a random one from the list of all libraries
                if (mutationType.equalsIgnoreCase("swap") || individual.getNoLibraries() == 1){
                    int newLibraryPosition = random.nextInt(this.libraries.size());
                    Library newLibrary = this.libraries.get(newLibraryPosition);
                    individual.getLibraries().set(i, newLibrary);
                    individual.cleanLibraries();
                }else {
                    if (i == individual.getNoLibraries() - 1){
                        Collections.swap(individual.getLibraries(), i, i-1);
                    }else{
                        Collections.swap(individual.getLibraries(), i, i+1);
                    }
                }
            }
        }
        return individual;
    }

}
