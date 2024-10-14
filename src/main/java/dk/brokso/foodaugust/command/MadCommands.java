package dk.brokso.foodaugust.command;


import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
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

    private enum MakroType {FIBRE, PROTEIN}

    ;

    public MadCommands(List<Food> foods) {
        this.foods = foods;
    }

    @ShellMethod("find")
    public String find(String ord) {

        StringBuilder builder = new StringBuilder();

        for (Food food : foods) {

            if (food.getName().toUpperCase().contains(ord.toUpperCase())) {
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

        try {
            valgtId = Integer.parseInt(userValg);
            gram = Integer.parseInt(userGram);
        } catch (NumberFormatException e) {
            System.out.println(String.format("%1s er ikke gyldig", userValg));
            return;
        }

        Optional<Food> fundetFood = foods.stream().filter(f -> f.getId() == valgtId).findFirst();

        if (fundetFood.isPresent()) {
            Food fundet = fundetFood.get();
            fundet.setGram(gram);
            valgtMadMap.put(fundet.getId(), fundet);
            System.out.println(String.format("Tilføjet %s gram %s", fundet.getGram(), fundet.getName()));
            se();
        } else {
            System.out.println("Den valgte mad findes ikke (" + userValg + ").");
        }


    }


    @ShellMethod("fjern")
    public void fjern(String userValg) {


        int valgtId;

        try {
            valgtId = Integer.parseInt(userValg);
        } catch (NumberFormatException e) {
            System.out.println(String.format("%1s er ikke gyldig", userValg));
            return;
        }

        Optional<Food> fjernetMad = Optional.of(valgtMadMap.remove(valgtId));

        if (fjernetMad.isPresent()) {
            System.out.println(String.format("Fjernet %s gram %s", fjernetMad.get().getGram(), fjernetMad.get().getName()));
        } else {
            System.out.println("Den valgte mad findes ikke (" + userValg + ").");
        }


    }

    @ShellMethod("fjernalt")
    public void fjernalt() {

        valgtMadMap.clear();

        System.out.println(String.format("Alle dine valg er fjernet"));

    }

    @ShellMethod("se")
    public void se() {

        Opskrift opskrift = new Opskrift(valgtMadMap.values().stream().toList());

        System.out.println(opskrift.toAsciiTable());

    }

    @ShellMethod("protein")
    public String protein() {


        Collections.sort(foods, (o1, o2) -> {
            return Float.compare(o2.getProteinIn100Gram(), o1.getProteinIn100Gram()); // Descending order
        });

        return foodsAsTable(MakroType.PROTEIN);
    }

    @ShellMethod("ny")
    public String ny() {
        Scanner scanner = new Scanner(System.in);
        Food food = new Food();


        System.out.println("Indtast navn");
        food.setName( scanner.nextLine());

        System.out.println("Indtast kcal pr 1000 gram");
        food.setKcalIn100Gram(Integer.parseInt(scanner.nextLine()));

        return food.toString();

    }
@ShellMethod("fibre")
    public String fibre() {


        Collections.sort(foods, (o1, o2) -> {
            return Float.compare(o2.getDietaryfibreIn100gram(), o1.getDietaryfibreIn100gram()); // Descending order
        });

        return foodsAsTable(MakroType.FIBRE);
    }

    private String foodsAsTable(MakroType type) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("Id", "Navn", "Kalorier", "Protein (g)", "Kulhydrat (g)", "Fedt (g)", "Fiber (g)");
        table.addRule();

        for (Food food : foods) {
            if (MakroType.FIBRE == type && food.getDietaryfibreIn100gram() > 0 || MakroType.PROTEIN == type && food.getProteinIn100Gram() > 0) {
                table.addRow(food.getId(), food.getName(), food.getKcalIn100Gram(), food.getProteinIn100Gram(), food.getCarbonhydratesIn100Gram(), food.getFatIn100Gram(), food.getDietaryfibreIn100gram());
            }
            else{
                continue;
            }
            table.addRule();
        }
        table.getRenderer().setCWC(new CWC_LongestLine());
        return table.render();
    }
}
