package org.example.console;

import org.example.console.main.menu.Menus;

public class Console {

    Menus menus = new Menus();

    public static void main(String[] args){
      new Console();
    }

    public Console() {
        menus.start();
    }

}
