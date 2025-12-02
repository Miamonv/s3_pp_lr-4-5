package persistence;
import model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileToyRepository {

    public List<Toy> loadToys(String filename) {
        List<Toy> loadedToys = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split(",");

                String type = p[0];
                String name = p[1];
                double price = Double.parseDouble(p[2]);
                int minAge = Integer.parseInt(p[3]);
                int maxAge = Integer.parseInt(p[4]);

                // Factory logic (Фабрика об'єктів)
                switch (type) {
                    case "Transport":
                        int speed = Integer.parseInt(p[5]);
                        String color = p[6];
                        loadedToys.add(new Transport(name, price, minAge, maxAge, speed, color));
                        break;
                    case "Doll":
                        // додати логіку для ляльки
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено: " + filename);
        }
        return loadedToys;
    }
}