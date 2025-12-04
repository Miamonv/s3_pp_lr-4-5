package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static model.TransportType.CAR;

class ModelTest {

    @Test
    void testAddToy() {
        GameRoom room = new GameRoom("Test Room", 500.0);

        Toy doll = new Doll("Barbie", 100.0, 5, 10, Size.SMALL, "Plastic", "Blonde");
        room.addToy(doll);

        Assertions.assertEquals(100.0, room.getCurrentSpent());
        Assertions.assertEquals(400.0, room.getRemainingBudget());
        Assertions.assertEquals("Barbie", doll.getName());
        Assertions.assertEquals(Size.SMALL, doll.getSize());
        Assertions.assertEquals(10, doll.getMaxAge());
        Assertions.assertEquals(5, doll.getMinAge());
    }

    @Test
    void testGetBudgetLimit() {
        GameRoom room = new GameRoom("Test Room", 300.0);
        Assertions.assertEquals(300.0, room.getBudgetLimit());
    }

    @Test
    void testGetToys() {
        GameRoom room = new GameRoom("Test Room", 400.0);

        Toy car = new Transport("Hot Wheels", 50.0, 3, 7, Size.SMALL, CAR, 15);
        Toy lego = new Lego("Star Wars", 150.0, 8, 14, Size.MEDIUM, "minecraft", 200);

        room.addToy(car);
        room.addToy(lego);

        Assertions.assertEquals(2, room.getToys().size());
        Assertions.assertEquals(car, room.getToys().get(0));
        Assertions.assertEquals(lego, room.getToys().get(1));

        Assertions.assertEquals("Кімната 'Test Room' | Бюджет: 200,00 / 400,00 | Іграшок: 2", room.toString());
    }

    @Test
    void testSpent() {
        GameRoom room = new GameRoom("Test Room", 300.0);
        Assertions.assertEquals(300.0, room.getBudgetLimit());
        Toy TransportToy = new Transport("Truck", 200.0, 4, 12, Size.MEDIUM, CAR, 20);
        room.addToy(TransportToy);
        Assertions.assertEquals("Test Room", room.getName());
        Assertions.assertEquals(200.0, room.getCurrentSpent());

        room.decreaseSpent(100.0);
        Assertions.assertEquals(100.0, room.getCurrentSpent());

        room.decreaseSpent(200.0);
        Assertions.assertEquals(0.0, room.getCurrentSpent());

        room.increaseSpent(150.0);
        Assertions.assertEquals(150.0, room.getCurrentSpent());
    }

    @Test
    void testToString() {
        GameRoom room = new GameRoom("Fun Zone", 600.0);
        Toy doll = new Doll("Dollie", 120.0, 6, 12, Size.MEDIUM, "Cloth", "Brown");
        room.addToy(doll);

        String expected1 = "Кімната 'Fun Zone' | Бюджет: 120,00 / 600,00 | Іграшок: 1";
        Assertions.assertEquals(expected1, room.toString());

        String expected2 = "Назва: %-20s | Ціна: %-8.2f грн | Вік: 6-12 | Розмір: %s | Матеріал: %s | Волосся: %s"
                .formatted("Dollie", 120.0, Size.MEDIUM, "Cloth", "Brown");
        Assertions.assertEquals(expected2, doll.toString());

        Toy lego = new Lego("Lego Set", 200.0, 8, 15, Size.LARGE, "City", 300);
        String expected3 = "Назва: %-20s | Ціна: %-8.2f грн | Вік: 8-15 | Розмір: %s | Тип: LEGO (%s) | Деталей: %d"
                .formatted("Lego Set", 200.0, Size.LARGE, "City", 300);
        Assertions.assertEquals(expected3, lego.toString());

        Toy transportToy = new Transport("Speedster", 80.0, 4, 10, Size.SMALL, CAR, 25);
        String expected4 = "Назва: %-20s | Ціна: %-8.2f грн | Вік: 4-10 | Розмір: %s | Вид: %s | Швидкість: %d км/год"
                .formatted("Speedster", 80.0, Size.SMALL, "Машинка", 25);
        Assertions.assertEquals(expected4, transportToy.toString());

        Toy toy = new Toy("Generic Toy", 60.0, 2, 6, Size.SMALL) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        String expected5 = "Назва: %-20s | Ціна: %-8.2f грн | Вік: 2-6 | Розмір: %s"
                .formatted("Generic Toy", 60.0, Size.SMALL);
        Assertions.assertEquals(expected5, toy.toString());
    }

    @Test
    void testSpecGetsSets() {
        Doll doll = new Doll("Dollie", 120.0, 6, 12, Size.MEDIUM, "Cloth", "Brown");
        Assertions.assertEquals("Cloth", doll.getMaterial());
        Assertions.assertEquals("Brown", doll.getHairColor());

        Lego lego = new Lego("Lego Set", 200.0, 8, 15, Size.LARGE, "City", 300);
        Assertions.assertEquals("City", lego.getSeries());
        Assertions.assertEquals(300, lego.getNumberOfPieces());

        Transport transportToy = new Transport("Speedster", 80.0, 4, 10, Size.SMALL, CAR, 25);
        Assertions.assertEquals(CAR, transportToy.getType());
        Assertions.assertEquals(25, transportToy.getMaxSpeed());

        Toy toy = new Toy("Generic Toy", 60.0, 2, 6, Size.SMALL) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        toy.setName("Tomas");
        Assertions.assertEquals("Tomas", toy.getName());
        toy.setPrice(75.0);
        Assertions.assertEquals(75.0, toy.getPrice());
    }
}

