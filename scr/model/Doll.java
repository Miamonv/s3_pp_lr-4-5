package model;

public class Doll extends Toy{
    String hairColor;

    public Doll(String name, double price, int minAge, int maxAge, String hairColor) {  //доробити enum
        super(name, price, minAge, maxAge);
        this.hairColor = hairColor;
    }

}
