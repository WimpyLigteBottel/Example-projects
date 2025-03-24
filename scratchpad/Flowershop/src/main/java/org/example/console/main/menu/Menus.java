package org.example.console.main.menu;

import org.example.console.main.menu.MainMenu;
import org.example.console.main.menu.CheckoutBasketMenu;
import org.example.console.main.menu.SuccessFailMenu;
import org.example.console.main.menu.DisplayFlowersMenu;
import org.example.console.main.menu.DetailedFlowerMenu;
import org.example.shop.FlowerShop;

public class Menus {

    public static MainMenu mainMenu = new MainMenu();
    public static CheckoutBasketMenu checkoutBasket = new CheckoutBasketMenu();
    public static DisplayFlowersMenu displayFlowers = new DisplayFlowersMenu();
    public static DetailedFlowerMenu detailedFlowerMenu = new DetailedFlowerMenu();
    public static SuccessFailMenu successFailMenu = new SuccessFailMenu();
    public  static FlowerShop flowerShop = new FlowerShop();

    public Menus() {
        mainMenu.checkoutBasket = checkoutBasket;

        checkoutBasket.flowerShop = flowerShop;
        checkoutBasket.successFailMenu = successFailMenu;
        checkoutBasket.mainmenu = mainMenu;

        successFailMenu.flowerShop = flowerShop;
        successFailMenu.mainmenu = mainMenu;



        mainMenu.displayFlowers = displayFlowers;
        displayFlowers.flowerShop =flowerShop;
        displayFlowers.mainmenu = mainMenu;
        displayFlowers.detailedFlowerMenu = detailedFlowerMenu;

        detailedFlowerMenu.displayFlowers = displayFlowers;
        detailedFlowerMenu.flowerShop = flowerShop;
        detailedFlowerMenu.mainMenu = mainMenu;
    }

    public void start(){
        mainMenu.display();
    }
}
