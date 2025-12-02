package model;

public enum TransportType {
    CAR("Машинка"),
    PLANE("Літак"),
    BOAT("Човен"),
    TRAIN("Потяг"),
    HELICOPTER("Гелікоптер");

    private final String label;

    TransportType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}