package util;
import model.*;

// перетворення рядка з файлу в об'єкт
public class ToyMapper {

    public static Toy stringToToy(String line) {
        String[] p = line.split(", ");
        String type = p[0].trim();
        String name = p[1].trim();
        double price = Double.parseDouble(p[2].trim());
        int minAge = Integer.parseInt(p[3].trim());
        int maxAge = Integer.parseInt(p[4].trim());
        Size size = Size.valueOf(p[5].trim()); // Enum: SMALL, MEDIUM, LARGE

        switch (type) {
            case "Transport":
                TransportType tType = TransportType.valueOf(p[6].trim());
                int speed = Integer.parseInt(p[7].trim());
                return new Transport(name, price, minAge, maxAge, size, tType, speed);
            case "Lego":
                String series = p[6].trim();
                int pieces = Integer.parseInt(p[7].trim());
                return new Lego(name, price, minAge, maxAge, size, series, pieces);
            case "Doll":
                String material = p[6].trim();
                String hairColor = p[7].trim();
                return new Doll(name, price, minAge, maxAge, size, material, hairColor);
            default:
                throw new IllegalArgumentException("Невідомий тип іграшки: " + type);
        }
    }

    // перетворення об'єкта в рядок (для збереження)
    public static String toyToString(Toy toy) {
        String baseInfo = String.format(java.util.Locale.US, "%s, %.2f, %d, %d, %s",
                toy.getName(),
                toy.getPrice(),
                toy.getMinAge(),
                toy.getMaxAge(),
                toy.getSize());

        if (toy instanceof Transport) {
            Transport t = (Transport) toy;
            // формат: Type, BaseInfo, TypeEnum, Speed
            return String.format("Transport, %s, %s, %d", baseInfo, t.getType(), t.getMaxSpeed());
        }
        else if (toy instanceof Lego) {
            Lego l = (Lego) toy;
            // формат: Type, BaseInfo, Series, Pieces
            return String.format("Lego, %s, %s, %d", baseInfo, l.getSeries(), l.getNumberOfPieces());
        }
        else if (toy instanceof Doll) {
            Doll d = (Doll) toy;
            // формат: Type, BaseInfo, Material, HairColor
            return String.format("Doll, %s, %s, %s", baseInfo, d.getMaterial(), d.getHairColor());
        }

        return "";
    }
}