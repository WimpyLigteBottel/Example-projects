package org.example.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowerShop {

    Map<Flower, Integer> inventory = new HashMap<>();
    Basket basket = new Basket();

    public FlowerShop() {
        inventory.put( new Flower("rose",0.5, "a beautiful rose"), 1);
        inventory.put( new Flower("tulip",0.5, "a tulip"), 1);
        inventory.put( new Flower("sunflower",0.5, "a shining sunflower"), 1);
    }

    public List<Flower> getAllFlowers() {
        return inventory.keySet()
                .stream()
                .toList();
    }

    public Flower findFlower(String input) {
        return inventory.keySet()
                .stream()
                .filter(
                    x-> x.name.equalsIgnoreCase(input)
                )
                .findFirst()
                .orElse(null);

    }

    public void addToBasket(Flower flower) {
        basket.flowers.putIfAbsent(flower.name,0);
        basket.flowers.put(flower.name, basket.flowers.get(flower.name)+1);
    }

    public void processBucket() {


    }

    public void clearBasket() {

    }
}
