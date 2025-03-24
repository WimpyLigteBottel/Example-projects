package org.example.console.main.menu;

import org.example.shop.FlowerShop;

import java.text.MessageFormat;
import java.util.Scanner;

public class SuccessFailMenu {

   public FlowerShop flowerShop;
    public MainMenu mainmenu;
    public void display(Scanner scanner) {
        String message = """
                Your checkout was {0}
                
                1. Continue
                """;

        message = MessageFormat.format(message, processOrder());
        System.out.println(message);
        String input = scanner.next();

        mainmenu.display(scanner);
    }



    public String processOrder(){
        try{
            flowerShop.processBucket();
            return "success";
        }catch (RuntimeException e){
            return "failed";
        }
    }
}
