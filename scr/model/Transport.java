package model;

public class Transport extends Toy{
    int maxSpeed;
    String color;

    Transport(String name, double price, int minAge, int maxAge, int maxSpeed, String color) {
        super(name, price, minAge, maxAge);
        this.maxSpeed = maxSpeed;
        this.color = color;
    }
}
