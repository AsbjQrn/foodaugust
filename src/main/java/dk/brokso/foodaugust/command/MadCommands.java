package dk.brokso.foodaugust.command;


import dk.brokso.foodaugust.data.Food;
import dk.brokso.foodaugust.data.Opskrift;
import dk.brokso.foodaugust.util.Loggable;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.*;

@ShellComponent
public class MadCommands implements Loggable {

    private final List<Food> foods;
    private final Map<Integer, Food> valgtMadMap = new HashMap<>();

    public MadCommands(List<Food> foods) {
        this.foods = foods;
    }

    @ShellMethod("find")
    public String find(String ord) {

        StringBuilder builder = new StringBuilder();

        for (Food food : foods){

            if (food.getName().toUpperCase().contains(ord.toUpperCase())){
                builder.append(food.getId());
                builder.append(" ");
                builder.append(food.getName());
                builder.append("\n");

            }

        }
        return builder.toString();

    }

    @ShellMethod("valg")
    public void valg(String userValg, String userGram) {


        int valgtId;
        int gram;

        try{
            valgtId = Integer.parseInt(userValg);
            gram = Integer.parseInt(userGram);
        } catch (NumberFormatException e){
            System.out.println(String.format("%1s er ikke gyldig", userValg));
            return;
        }

        Optional<Food> fundetFood = foods.stream().filter(f -> f.getId() == valgtId).findFirst();

        if(fundetFood.isPresent()){
            Food fundet = fundetFood.get();
            fundet.setGram(gram);
            valgtMadMap.put(fundet.getId(), fundet);
            System.out.println(String.format("Tilføjet %s gram %s", fundet.getGram() ,fundet.getName()));
            se();
        } else {
            System.out.println("Den valgte mad findes ikke (" + userValg + ").") ;
        }


    }

    @ShellMethod("se")
    public void se() {

        Opskrift opskrift = new Opskrift(valgtMadMap.values().stream().toList());

        System.out.println(opskrift.toAsciiTable());

    }
}
