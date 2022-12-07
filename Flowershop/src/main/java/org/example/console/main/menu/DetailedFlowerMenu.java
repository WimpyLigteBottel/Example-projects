package org.example.console.main.menu;

import org.example.shop.Flower;
import org.example.shop.FlowerShop;

import java.text.MessageFormat;
import java.util.Scanner;

public class DetailedFlowerMenu {

    public FlowerShop flowerShop;
    public DisplayFlowersMenu displayFlowers;
    public MainMenu mainMenu;

    public void display(Scanner scanner, Flower flower) {
        String message = """
                Flower shop:
                name:{0}
                price{1}
                description:{2}
                
                1.Add to basket
                2.Back
                """;

        message = MessageFormat.format(message,flower.getName(),flower.getPrice(),flower.getDescription());
        System.out.println(message);
        String input = scanner.next();

        switch (input) {
            case "back" -> displayFlowers.display(scanner);
            case "1" -> {
                flowerShop.addToBasket(flower);
                mainMenu.display(scanner);
            }
            default -> display(scanner,flower);
        }
    }
}
