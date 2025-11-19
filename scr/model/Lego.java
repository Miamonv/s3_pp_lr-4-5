package model;

public class Lego extends Toy{
    String topic;    //тематика наприклад ніндзя, майн, барбі і тд

    public  Lego(String name, int price, int minAge, int maxAge, String topic){
        super(name, price, minAge, maxAge);
        this.topic = name;
    }
}
