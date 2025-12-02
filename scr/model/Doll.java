package model;

public class Doll extends Toy {
    private String material;
    String hairColor;

    public Doll(String name, double price, int minAge, int maxAge, Size size, String material, String hairColor) {
        super(name, price, minAge, maxAge, size);
        this.material = material;
        this.hairColor = hairColor;
    }

    public String getMaterial() { return material; }
    public String getHairColor() { return hairColor; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Матеріал: %s | Волосся: %s", material, hairColor);
    }
}