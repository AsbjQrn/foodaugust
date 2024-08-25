package dk.brokso.foodaugust.data;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Food {

    private static int foodcounter;
    private int id;
    private String name;
    private float kcalIn100Gram;
    private float proteinIn100Gram;
    private float fatIn100Gram;
    private float carbonhydratesIn100Gram;
    private float dietaryfibreIn100gram;
    private int gram;
    private int makronaeringVaegt;

    public Food(){}

    private Food(String name, float kcalIn100Gram, float proteinIn100Gram, float fatIn100Gram, float carbonhydratesIn100Gram, float dietaryfibreIn100gram) {
        this.name = name;
        this.kcalIn100Gram = kcalIn100Gram;
        this.proteinIn100Gram = proteinIn100Gram;
        this.fatIn100Gram = fatIn100Gram;
        this.carbonhydratesIn100Gram = carbonhydratesIn100Gram;
        this.dietaryfibreIn100gram = dietaryfibreIn100gram;
    }


    public static Food of(List<String> foodProps) {

        foodcounter++;

        Food food = new Food();

        food.setId(foodcounter);
        food.setName(foodProps.get(0));
        food.setKcalIn100Gram(localParseFloat(foodProps, 5));
        food.setProteinIn100Gram(localParseFloat(foodProps, 7));
        food.setCarbonhydratesIn100Gram(localParseFloat(foodProps, 10));
        food.setDietaryfibreIn100gram(localParseFloat(foodProps, 13));
        food.setFatIn100Gram(localParseFloat(foodProps, 14));


        return food;

    }

    private static float localParseFloat(List<String> foodProps, int position) {
        String foodprop = foodProps.get(position);

        if ("".equals(foodprop.trim())) {
            foodprop = "0";
        }

        Float f = 0f;

        try {
            f = Float.parseFloat(foodProps.get(position));
        } catch (Exception e) {
            System.out.println("");
        }

        return f;

    }

    public float getTotalCalories(){
        return (kcalIn100Gram * gram)/100 ;
    }

    public float getGramProtein(){
        return (proteinIn100Gram * gram)/100;
    }

    public float getGramCarbonhydrates(){
        return carbonhydratesIn100Gram * gram  * 1/100;
    }

    public float getGramFat(){
        return fatIn100Gram * gram  * 1/100;
    }

    public float getGramDietaryfibre(){
        return dietaryfibreIn100gram * gram  * 1/100;
    }

    public float getMakroNaeringsVaegt(){
        return getGramProtein() + getGramFat() + getGramCarbonhydrates();
    }







}
