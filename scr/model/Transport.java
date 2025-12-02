package model;

public class Transport extends Toy {
    private TransportType type;
    private int maxSpeed;

    public Transport(String name, double price, int minAge, int maxAge, Size size, TransportType type, int maxSpeed) {
        super(name, price, minAge, maxAge, size);
        this.type = type;
        this.maxSpeed = maxSpeed;
    }

    public TransportType getType() { return type; }
    public int getMaxSpeed() { return maxSpeed; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Вид: %s | Швидкість: %d км/год", type.getLabel(), maxSpeed);
    }
}