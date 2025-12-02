package persistence;
import model.Toy;
import java.util.ArrayList;

public class ToyRepository {
    ArrayList<Toy> toyStorage;

    public ToyRepository(ArrayList<Toy> fileToyStorage) {
        this.toyStorage = new ArrayList<>(fileToyStorage);
    }

    public void showToyRepository() {
        for (Toy toy : toyStorage) {
            toy.showString();
        }
    }

    public ArrayList<Toy> getToyStorage() {

        return toyStorage;
    }


}
