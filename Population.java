package lab3;

import java.util.ArrayList;
public class Population {
    private ArrayList<Chromosome> population;
    public Population(){
        population = new ArrayList<>();
        for (int i = 0; i < Main.POPULATION_SIZE; i++){
            population.add(new Chromosome(i));
        }
    }
    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}

