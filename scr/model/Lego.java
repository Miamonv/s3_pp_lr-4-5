package model;

public class Lego extends Toy {
    private String series;       // наприклад "Star Wars", "Technic", "City"
    private int numberOfPieces;  // кількість деталей (для сортування за складністю)

    public Lego(String name, double price, int minAge, int maxAge, Size size, String series, int numberOfPieces) {
        super(name, price, minAge, maxAge, size);
        this.series = series;
        this.numberOfPieces = numberOfPieces;
    }

    public String getSeries() { return series; }
    public int getNumberOfPieces() { return numberOfPieces; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Тип: LEGO (%s) | Деталей: %d", series, numberOfPieces);
    }
}