package lab3;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
public class Main {
    public final static int MIN_COST = 2;
    public final static int MAX_COST = 10;
    public final static int MIN_WEIGHT = 1;
    public final static int MAX_WEIGHT = 5;
    public final static int MAX_BACKPACK_WEIGHT = 150;
    public final static int THINGS_COUNT = 100;
    public final static int POPULATION_SIZE = 100;
    public final static double MUTATION_CHANCE = 0.05;
    public static int TOURNAMENT_NUMBER = 20;
    public static int numberOfGenerations = 3;
    public static ArrayList<Thing> thingsCollection;
    // прибрати найслабшу особину - найнижча загальна цінність предметів
    public static void removeTheWeakest(Population population) {
        int worstCost = THINGS_COUNT * MAX_COST;
        int index = -1;
        for (Chromosome chromosome : population.getPopulation()) {
            if (chromosome.getCost() < worstCost) {
                worstCost = chromosome.getCost();
                index = population.getPopulation().indexOf(chromosome);
            }
        }
        population.getPopulation().remove(index);
    }
    // отримати зі списку особину з найвищою загальною цінністю
    public static Chromosome getBestCost(ArrayList<Chromosome> candidates) {
        int maxCost = 0;
        Chromosome bestCandidate = new Chromosome();
        for (Chromosome candidate : candidates) {
            if (candidate.getCost() > maxCost) {
                maxCost = candidate.getCost();
                bestCandidate = candidate;
            }
        }
        return bestCandidate;
    }
    // турнірний відбір 2 батьків:
    // 1) із заданої кількості випадкових особин обирається та, що з найвищою цінністю
    // 2) зі всієї популяції обирається випадкова особина
    public static ArrayList<Chromosome> select(Population population) {
        ArrayList<Chromosome> parents = new ArrayList<>();
        ArrayList<Chromosome> candidates = new ArrayList<>();
        Random rand = new Random();
        int randomInt = 0;
        while (candidates.size() != TOURNAMENT_NUMBER) {
            randomInt = rand.nextInt(population.getPopulation().size());
            Chromosome candidate =
                    population.getPopulation().get(randomInt);
            if (!candidates.contains(candidate)) {
                candidates.add(population.getPopulation().get(randomInt));
            }
        }
        parents.add(Main.getBestCost(candidates));
        randomInt = rand.nextInt(population.getPopulation().size());
        Chromosome secondParent = population.getPopulation().get(randomInt);
        while (Objects.equals(parents.get(0), secondParent)) {
            randomInt = rand.nextInt(population.getPopulation().size());
            secondParent = population.getPopulation().get(randomInt);
        }
        parents.add(secondParent);
        return parents;
    }
    // схрещуються дві особини: оператор схрещування рівномірний
    // генерується маска на 1 біт, відповідно до результату якого
    // особина отримує ген від одного з батьків
    public static ArrayList<Chromosome> cross(ArrayList<Chromosome> parents)
    {
        ArrayList<Chromosome> children = new ArrayList<>();
        Chromosome child1 = new Chromosome();
        Chromosome child2 = new Chromosome();
        Random randInt = new Random();
        for (int i = 0; i < THINGS_COUNT; i++) {
            int mask = randInt.nextInt(2);
            if (mask == 0) {
                child1.getChromosome().set(i,
                        parents.get(0).getChromosome().get(i));
                child2.getChromosome().set(i,
                        parents.get(1).getChromosome().get(i));
            } else {
                child1.getChromosome().set(i,
                        parents.get(1).getChromosome().get(i));
                child2.getChromosome().set(i,
                        parents.get(0).getChromosome().get(i));
            }
        }
        children.add(child1);
        children.add(child2);
        return children;
    }
// мутація гену: два випадкові гени міняються місцями
    // відповідно до особливостей релізації у двох генах змінюється присутність "речі" у "рюкзаку"
    // приклад: якщо у першому гені вона була, а у другому - ні, то стало навпаки
    public static Chromosome mutate(Chromosome chromosome) {
        if (Math.random() < MUTATION_CHANCE) {
            Random rand = new Random();
            int randInt1 = rand.nextInt(THINGS_COUNT);
            int randInt2 = rand.nextInt(THINGS_COUNT);
            while (randInt2 == randInt1){
                randInt2 = rand.nextInt(THINGS_COUNT);
            }
            int tmp = chromosome.getChromosome().get(randInt1);
            chromosome.getChromosome().set(randInt1,
                    chromosome.getChromosome().get(randInt2));
            chromosome.getChromosome().set(randInt2, tmp);
        }
        return chromosome;
    }
// локальне покращення:
    // аналіз додавання у рюкзак на місце пустого гену найціннішої "речі"
    // результат: один ген особини змінюється відповідно до найкращого результату
    public static Chromosome improve(Chromosome chromosome) {
        int initialWeight = chromosome.getWeight();
        int initialCost = chromosome.getCost();
        int bestCost = initialCost;
        int bestIndex = -1;
        for (int i = 0; i < THINGS_COUNT; i++) {
            if (chromosome.getChromosome().get(i) == 0) {
                if (initialWeight + Main.thingsCollection.get(i).getWeight()
                        <= MAX_BACKPACK_WEIGHT) {
                    if (bestCost < initialCost +
                            Main.thingsCollection.get(i).getCost()) {
                        bestCost = initialCost +
                                Main.thingsCollection.get(i).getCost();
                        bestIndex = i;
                    }
                }
            }
        }
        if (bestIndex != -1){
            chromosome.getChromosome().set(bestIndex, 1);
        }
        return chromosome;
    }
    public static boolean isAlive(Chromosome chromosome) {
        return chromosome.getWeight() <= MAX_BACKPACK_WEIGHT;
    }
    public static void launch(){
        Population population = new Population();
        for (int i = 0; i < numberOfGenerations; i++) {
            ArrayList<Chromosome> parents = Main.select(population);
            ArrayList<Chromosome> children = Main.cross(parents);
            Chromosome child1 = Main.mutate(children.get(0));
            Chromosome child2 = Main.mutate(children.get(1));
            if (Main.isAlive(child1)) {
                Main.removeTheWeakest(population);
                child1 = Main.improve(child1);
                population.getPopulation().add(child1);
            }
            if (Main.isAlive(child2)) {
                Main.removeTheWeakest(population);
                child2 = Main.improve(child2);
                population.getPopulation().add(child2);
            }
        }
        Chromosome chromosome = Main.getBestCost(population.getPopulation());
        // System.out.println(numberOfGenerations + ";" + chromosome.getWeight() + ";" + chromosome.getCost());
        System.out.println("Number of generations = " + numberOfGenerations);
        System.out.println("Tournament number = " + TOURNAMENT_NUMBER);
        System.out.println("Weight = " + chromosome.getWeight());
        System.out.println("Cost = " + chromosome.getCost());
    }
    public static void main(String[] args) {
        thingsCollection = new ArrayList<>();
        for (int i = 0; i < THINGS_COUNT; i++) {
            thingsCollection.add(new Thing());
        }
// while (numberOfGenerations <= 1000){
// Main.launch();
// numberOfGenerations += 20;
// }
// numberOfGenerations = 1500;
// Main.launch();
// numberOfGenerations = 2000;
// Main.launch();
// numberOfGenerations = 3000;
// Main.launch();
// numberOfGenerations = 4000;
// Main.launch();
// numberOfGenerations = 5000;
// Main.launch();
// numberOfGenerations = 8000;
// Main.launch();
// numberOfGenerations = 10000;
// Main.launch();
        numberOfGenerations = 250;
        TOURNAMENT_NUMBER = 10;
        Main.launch();
    }
}
