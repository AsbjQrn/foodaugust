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
    private double kcalIn100Gram;
    private double proteinIn100Gram;
    private double fatIn100Gram;
    private double carbonhydratesIn100Gram;
    private double dietaryfibreIn100gram;
    private int gram;
    private int makronaeringVaegt;

    public Food(){}

    private Food(String name, double kcalIn100Gram, double proteinIn100Gram, double fatIn100Gram, double carbonhydratesIn100Gram, double dietaryfibreIn100gram) {
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
        food.setKcalIn100Gram(localParsedouble(foodProps, 5));
        food.setProteinIn100Gram(localParsedouble(foodProps, 7));
        food.setCarbonhydratesIn100Gram(localParsedouble(foodProps, 10));
        food.setDietaryfibreIn100gram(localParsedouble(foodProps, 13));
        food.setFatIn100Gram(localParsedouble(foodProps, 14));


        return food;

    }

    public static Food ofAdditional(List<String> foodProps) {

        foodcounter++;

        Food food = new Food();

        food.setId(foodcounter);
        food.setName(foodProps.get(0));
        food.setKcalIn100Gram(localParsedouble(foodProps, 1));
        food.setProteinIn100Gram(localParsedouble(foodProps, 2));
        food.setCarbonhydratesIn100Gram(localParsedouble(foodProps, 3));
        food.setDietaryfibreIn100gram(localParsedouble(foodProps, 4));
        food.setFatIn100Gram(localParsedouble(foodProps, 5));


        return food;

    }

    private static double localParsedouble(List<String> foodProps, int position) {
        String foodprop = foodProps.get(position);

        if ("".equals(foodprop.trim())) {
            foodprop = "0";
        }

        double f = 0f;

        try {
            f = Double.parseDouble(foodProps.get(position));
        } catch (Exception e) {
            System.out.println("");
        }

        return f;

    }

    public double getTotalCalories(){
        return (kcalIn100Gram * gram)/100 ;
    }

    public double getGramProtein(){
        return (proteinIn100Gram * gram)/100;
    }

    public double getGramCarbonhydrates(){
        return carbonhydratesIn100Gram * gram  * 1/100;
    }

    public double getGramFat(){
        return fatIn100Gram * gram  * 1/100;
    }

    public double getGramDietaryfibre(){
        return dietaryfibreIn100gram * gram  * 1/100;
    }

    public double getMakroNaeringsVaegt(){
        return getGramProtein() + getGramFat() + getGramCarbonhydrates();
    }







}
