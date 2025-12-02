package model;

public abstract class Toy {
    private String name;
    private double price;
    private int minAge;
    private int maxAge;
    private Size size;

    public Toy(String name, double price, int minAge, int maxAge, Size size) {
        this.name = name;
        this.price = price;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.size = size;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
    public Size getSize() { return size; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return String.format("Назва: %-20s | Ціна: %-8.2f грн | Вік: %d-%d | Розмір: %s",
                name, price, minAge, maxAge, size);
    }
}