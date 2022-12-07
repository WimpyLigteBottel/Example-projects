package org.example.console.main.menu;

import org.example.console.main.menu.CheckoutBasketMenu;
import org.example.console.main.menu.DisplayFlowersMenu;

import java.util.Scanner;

public class MainMenu {

    public DisplayFlowersMenu displayFlowers;
    public CheckoutBasketMenu checkoutBasket;

    public void display(Scanner scanner){
        String menu = """
                Welcome to the flower shop
                1. Display all flowers
                2. Checkout basket
                """;
        System.out.println(menu);
        String next = scanner.next();

        switch (next) {
            case "1" -> displayFlowers.display(scanner);
            case "2" -> checkoutBasket.display(scanner);
            default -> display(scanner);
        }
    }

    public void display(){
        display(new Scanner(System.in));
    }
}
