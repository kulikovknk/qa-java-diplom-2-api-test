package generator;

import dto.OrderRequest;

import java.util.ArrayList;
import java.util.Random;

public class OrderRequestGenerator {

    public static OrderRequest getNewOrderRequestWithIngredients(ArrayList<ArrayList<String>> ingredientsList) {

        ArrayList<String> bunsList = ingredientsList.get(0);
        ArrayList<String> saucesList = ingredientsList.get(1);
        ArrayList<String> fillingsList = ingredientsList.get(2);

        // массив ингредиентов, используем по одному ингредиенту каждого типа
        String[] ingredients = new String[3];

        // ингредиент каждого типа выбираем из списка в случайном порядке
        ingredients[0] = bunsList.get(new Random().nextInt(bunsList.size()));
        ingredients[1] = saucesList.get(new Random().nextInt(saucesList.size()));
        ingredients[2] = fillingsList.get(new Random().nextInt(fillingsList.size()));

        return new OrderRequest(ingredients);

    }

    public static OrderRequest getNewOrderRequestWithInvalidIngredients() {

        // массив ингредиентов, используем по одному ингредиенту каждого типа
        String[] ingredients = new String[3];

        // заполним массив ингредиентами с неправильным хешем
        ingredients[0] = "111111111111111111111111";
        ingredients[1] = "222222222222222222222222";
        ingredients[2] = "333333333333333333333333";

        return new OrderRequest(ingredients);

    }

    public static OrderRequest getNewOrderRequestWithNoIngredients() {

        // заказ с пустыми ингредиентами
        return new OrderRequest(new String[3]);

    }

}
