package service;

import model.Flower;
import exception.InvalidBudgetException;

import java.util.*;
import java.util.stream.Collectors;

public class BouquetCreator {

    public List<Flower> createBouquet(List<Flower> availableFlowers, int budget) throws InvalidBudgetException {
        List<Flower> bouquet = new ArrayList<>();
        Random random = new Random();

        int finalBudget = budget;
        List<Flower> affordableFlowers = availableFlowers.stream()
                .filter(f -> f.getPrice() <= finalBudget)
                .collect(Collectors.toList());

        if (affordableFlowers.isEmpty()) {
            throw new InvalidBudgetException("Невозможно составить букет на сумму " + budget);
        }

        // Пока хватает на хотя бы один из цветов
        while (budget >= affordableFlowers.stream().mapToInt(Flower::getPrice).min().orElse(Integer.MAX_VALUE)) {
            Flower chosen = affordableFlowers.get(random.nextInt(affordableFlowers.size()));
            if (budget >= chosen.getPrice()) {
                bouquet.add(chosen);
                budget -= chosen.getPrice();
            }
        }

        return bouquet;
    }

    public void sortBouquet(List<Flower> bouquet, String sortBy) {
        if ("Цена".equalsIgnoreCase(sortBy)) {
            bouquet.sort(Comparator.comparingInt(Flower::getPrice));
        } else if ("Название".equalsIgnoreCase(sortBy)) {
            bouquet.sort(Comparator.comparing(Flower::getName));
        }
    }

    public long countFlowersByType(List<Flower> bouquet, String type) {
        return bouquet.stream().filter(f -> f.getName().equalsIgnoreCase(type)).count();
    }
}
