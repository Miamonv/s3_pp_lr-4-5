package logic;

import model.GameRoom;
import model.Toy;
import persistence.FileToyRepository;
import java.util.List;
import java.util.stream.Collectors;

public class GameRoomService {
    private GameRoom activeRoom;
    private List<Toy> catalog;  //каталог всіх іграшок

    public GameRoomService(String filename) {
        FileToyRepository repo = new FileToyRepository(filename);
        this.catalog = repo.loadCatalog();
    }

    public void createRoom(String name, double budget) {
        this.activeRoom = new GameRoom(name, budget);
    }

    public GameRoom getActiveRoom() {
        return activeRoom;
    }

    /**
     * Головний метод пошуку:
     * 1. Приймає вік дитини (для якої купуємо зараз).
     * 2. Перевіряє, чи вистачить грошей у кімнаті.
     */
    public List<Toy> getToysForChild(int childAge) {
        if (activeRoom == null) return List.of();

        double moneyLeft = activeRoom.getRemainingBudget();

        return catalog.stream()
                //вік
                .filter(toy -> childAge >= toy.getMinAge() && childAge <= toy.getMaxAge())

                //бюджет
                .filter(toy -> toy.getPrice() <= moneyLeft)

                //вертаємо список
                .collect(Collectors.toList());
    }

    public void addToyToRoom(Toy toy) throws Exception {
        if (activeRoom.getRemainingBudget() < toy.getPrice()) {
            throw new Exception("Недостатньо коштів!");
        }
        activeRoom.addToy(toy);
    }

    public boolean removeToy(String toyName) {
        // Знаходимо іграшку для видалення
        Toy toyToRemove = gameRoom.getToys().stream()
                .filter(t -> t.getName().equalsIgnoreCase(toyName))
                .findFirst()
                .orElse(null);

        if (toyToRemove != null) {
            gameRoom.getToys().remove(toyToRemove);
            gameRoom.decreaseSpent(toyToRemove.getPrice());
            return true;
        }
        return false;
    }

    /**
     * Сортування всіх іграшок за ЦІНОЮ (зростання).
     */
    public void sortByPrice() {
        gameRoom.getToys().sort(Comparator.comparingDouble(Toy::getPrice));
    }

    /**
     * Сортування всіх іграшок за РОЗМІРОМ (Small -> Medium -> Large).
     * Працює завдяки порядку в Enum Size.
     */
    public void sortBySize() {
        gameRoom.getToys().sort(Comparator.comparing(Toy::getSize));
    }

    /**
     * Специфічне сортування: Тільки Транспорт за Швидкістю.
     * Повертає окремий відсортований список, не змінюючи основний порядок кімнати.
     */
    public List<Transport> getTransportsSortedBySpeed() {
        return gameRoom.getToys().stream()
                .filter(t -> t instanceof Transport) // Відбираємо тільки транспорт
                .map(t -> (Transport) t)             // Перетворюємо Toy на Transport
                .sorted(Comparator.comparingInt(Transport::getMaxSpeed).reversed()) // Від швидших до повільних
                .collect(Collectors.toList());
    }

    /**
     * Пошук іграшок за діапазоном параметрів (Ціна + Вікова група).
     */
    public List<Toy> findToys(double maxPrice, int minAge, int maxAge) {
        return gameRoom.getToys().stream()
                .filter(t -> t.getPrice() <= maxPrice)
                .filter(t -> t.getMinAge() >= minAge && t.getMaxAge() <= maxAge)
                .collect(Collectors.toList());
    }

    /**
     * Отримати загальну статистику.
     */
    public String getStatistics() {
        long transportCount = gameRoom.getToys().stream().filter(t -> t instanceof Transport).count();
        long legoCount = gameRoom.getToys().stream().filter(t -> t instanceof Lego).count();

        return String.format("Всього іграшок: %d (Транспорт: %d, Lego: %d). Витрачено: %.2f / %.2f",
                gameRoom.getToys().size(), transportCount, legoCount,
                gameRoom.getCurrentSpent(), gameRoom.getBudgetLimit());
    }
}