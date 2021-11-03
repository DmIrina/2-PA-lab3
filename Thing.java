package lab3;

public class Thing {
    private int cost;
    private int weight;
    public Thing(){
        this.cost = (int) (Math.random()*(Main.MAX_COST - Main.MIN_COST) +
                Main.MIN_COST);
        this.weight = (int) (Math.random()*(Main.MAX_WEIGHT -
                Main.MIN_WEIGHT) + Main.MIN_WEIGHT);
    }
    public int getCost() {
        return cost;
    }
    public int getWeight() {
        return weight;
    }
}

