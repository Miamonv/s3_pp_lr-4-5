package model;

import java.util.Scanner;

public abstract class Toy {
    String name;
    double price;
    int minAge;
    int maxAge;

    Toy(String name, double price, int minAge, int maxAge) {
        this.name = name;
        Scanner sc = new Scanner(System.in);
        while (true){
            if (price < 0){
                System.out.println("Ціна не може бути від'ємною\nВведіть правильну ціну:\n");
                sc.nextLine();
            }
            else break;
        }
        this.price = price;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

}
