package model;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private String name;
    private double budgetLimit;
    private double currentSpent;
    private List<Toy> toys;     // список іграшок у кімнаті

    public GameRoom(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.currentSpent = 0;
        this.toys = new ArrayList<>();
    }

    //додавання іграшки до кімнати
    public void addToy(Toy toy) {
        toys.add(toy);
        currentSpent += toy.getPrice();
    }

    public double getBudgetLimit() { return budgetLimit; }
    public double getCurrentSpent() { return currentSpent; }
    public List<Toy> getToys() { return toys; }
    public String getName() { return name; }

    //скільки залишилось бюджету
    public double getRemainingBudget() {
        return budgetLimit - currentSpent;
    }

    @Override
    public String toString() {
        return String.format("Кімната '%s' | Бюджет: %.2f / %.2f | Іграшок: %d",
                name, currentSpent, budgetLimit, toys.size());
    }
}