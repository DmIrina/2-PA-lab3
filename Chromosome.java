package lab3;

import java.util.ArrayList;
public class Chromosome implements Comparable<Chromosome>{
    private ArrayList<Integer> chromosome;
    public Chromosome(){
        chromosome = new ArrayList<>(Main.THINGS_COUNT);
        for (int i = 0; i < Main.THINGS_COUNT; i++){
            chromosome.add(0);
        }
    }
    public Chromosome(int initPosition){
        chromosome = new ArrayList<>(Main.THINGS_COUNT);
        for (int i = 0; i < Main.THINGS_COUNT; i++){
            chromosome.add(0);
        }
        chromosome.set(initPosition, 1);
    }
    public ArrayList<Integer> getChromosome() {
        return chromosome;
    }
    public Integer getCost(){
        int cost = 0;
        for (int i = 0; i < Main.THINGS_COUNT; i++) {
            if(this.chromosome.get(i) == 1){
                cost += Main.thingsCollection.get(i).getCost();
            }
        }
        return cost;
    }
    public Integer getWeight(){
        int weight = 0;
        for (int i = 0; i < Main.THINGS_COUNT; i++) {
            if(this.chromosome.get(i) == 1){
                weight += Main.thingsCollection.get(i).getWeight();
            }
        }
        return weight;
    }
    @Override
    public int compareTo(Chromosome c) {
        return this.getCost() - c.getCost();
    }
}