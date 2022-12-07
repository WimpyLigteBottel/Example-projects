package org.example.console.main.menu;

import org.example.shop.Flower;
import org.example.shop.FlowerShop;

import java.util.Scanner;
import java.util.stream.Collectors;

public class DisplayFlowersMenu {

    public FlowerShop flowerShop;
    public MainMenu mainmenu;
    public DetailedFlowerMenu detailedFlowerMenu;

    public void display(Scanner scanner) {

        String head = "Flower shop: \n";

        String middle =  flowerShop.getAllFlowers()
                .stream()
                .map(x-> x+"\n")
                .collect(Collectors.joining());

        String bottom = """
                XXXXXXXXXXXXXXXXX
                1. (enter flower name)
                2. Back
                """;

        String menu = head + middle + bottom;

        System.out.println(menu);
        String input = scanner.next();

        switch (input) {
            case "2" -> mainmenu.display(scanner);
            default -> {
                Flower flower = flowerShop.findFlower(input);

                if(flower != null){
                    detailedFlowerMenu.display(scanner, flower);
                }
                display(scanner);
            }
        }

    }

}
