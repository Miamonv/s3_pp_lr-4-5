package model;

public class GameRoom {
    int id;
    double budget;

    public  GameRoom(int id, double budget) {
        this.id = id;   // чи треба взагалі ід?
        while (true) {
            if (budget <= 0) {
                System.out.println("Бюджет не може бути від'ємним");
            }
            else break;
        }
        this.budget = budget;
    }
}
