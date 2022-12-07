package org.example.console.main.menu;

import org.example.shop.FlowerShop;

import java.text.MessageFormat;
import java.util.Scanner;

public class CheckoutBasketMenu {

    public FlowerShop flowerShop;
    public SuccessFailMenu successFailMenu;
    public MainMenu mainmenu;
    public void display(Scanner scanner) {
        String message = """
                Do you want to buy the following?
                
                {0}
                
                1.process basket
                2.Clear basket
                3.Back
                """;

        message = MessageFormat.format(message, getBasket());
        System.out.println(message);
        String input = scanner.next();


        switch (input) {
            case "1" -> {
                successFailMenu.display(scanner);
            }
            case "2" -> {
                flowerShop.clearBasket();
                mainmenu.display(scanner);
            }
            case "3" -> mainmenu.display(scanner);
            default -> display(scanner);
        }
    }


    public String getBasket(){


        return "";
    }
}
