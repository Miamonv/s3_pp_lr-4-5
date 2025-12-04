package util;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToyMapperTest {

    @Test
    void testStringToToyTransport() {
        String line = "Transport, HotWheels, 50.50, 3, 10, SMALL, CAR, 100";
        Toy result = ToyMapper.stringToToy(line);

        // перевірка чи створився правильний клас
        assertInstanceOf(Transport.class, result);
        Transport t = (Transport) result;

        // перевірка полів класу
        assertEquals("HotWheels", t.getName());
        assertEquals(50.50, t.getPrice());
        assertEquals(3, t.getMinAge());
        assertEquals(10, t.getMaxAge());
        assertEquals(Size.SMALL, t.getSize());
        assertEquals(TransportType.CAR, t.getType());
        assertEquals(100, t.getMaxSpeed());
    }

    @Test
    void testStringToToyLego() {
        String line = "Lego, StarWars, 500.0, 8, 99, MEDIUM, Galactic, 300";

        Toy result = ToyMapper.stringToToy(line);

        assertInstanceOf(Lego.class, result);
        Lego l = (Lego) result;

        assertEquals("StarWars", l.getName());
        assertEquals("Galactic", l.getSeries());
        assertEquals(300, l.getNumberOfPieces());
    }

    @Test
    void testStringToToyDoll() {
        String line = "Doll, Barbie, 120.0, 3, 7, SMALL, Plastic, Blonde";

        Toy result = ToyMapper.stringToToy(line);

        assertInstanceOf(Doll.class, result);
        Doll d = (Doll) result;

        assertEquals("Barbie", d.getName());
        assertEquals("Plastic", d.getMaterial());
        assertEquals("Blonde", d.getHairColor());
    }

    @Test
    void testStringToToyUnknownType() {
        // тип "Robot" не прописаний у switch
        String line = "Robot, Terminator, 1000.0, 10, 18, LARGE, Metal, 0";

        // має бути помилка IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToyMapper.stringToToy(line);
        });

        assertEquals("Невідомий тип іграшки: Robot", exception.getMessage());
    }

    @Test
    void testStringToToyInvalidFormat() {
        // якщо дані "биті" (наприклад, ціна не число)
        String line = "Transport, Car, ошвмпьмлвмсл, 3, 10, SMALL, CAR, 100";

        assertThrows(NumberFormatException.class, () -> {
            ToyMapper.stringToToy(line);
        });
    }

    @Test
    void testToyToStringTransport() {
        Transport t = new Transport("BMW", 200.0, 3, 8, Size.MEDIUM, TransportType.CAR, 150);

        String result = ToyMapper.toyToString(t);

        String expected = "Transport, BMW, 200.00, 3, 8, MEDIUM, CAR, 150";
        assertEquals(expected, result);
    }

    @Test
    void testToyToStringLego() {
        Lego l = new Lego("City", 300.50, 6, 12, Size.LARGE, "Police", 500);

        String result = ToyMapper.toyToString(l);

        String expected = "Lego, City, 300.50, 6, 12, LARGE, Police, 500";
        assertEquals(expected, result);
    }

    @Test
    void testToyToStringDoll() {
        Doll d = new Doll("Anna", 150.0, 3, 7, Size.SMALL, "Textile", "Red");

        String result = ToyMapper.toyToString(d);

        String expected = "Doll, Anna, 150.00, 3, 7, SMALL, Textile, Red";
        assertEquals(expected, result);
    }

    @Test
    void testToyToStringUnknownObject() {
        // клас який є Toy, але не Transport/Lego/Doll
        // перевірка 'return "";'
        Toy unknownToy = new Toy("Alien", 10.0, 1, 2, Size.SMALL) {
            // анонімний клас
        };

        String result = ToyMapper.toyToString(unknownToy);

        // має повернути порожній рядок, бо не знає такого класу
        assertEquals("", result);
    }
}