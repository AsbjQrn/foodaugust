package dk.brokso.foodaugust.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import dk.brokso.foodaugust.data.Food;
import dk.brokso.foodaugust.data.Opskrift;
import dk.brokso.foodaugust.util.Loggable;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@ShellComponent
public class MadCommands implements Loggable {

    private List<Food> foods;
    private final Map<Integer, Food> valgtMadMap = new HashMap<>();

    private enum MakroType {FIBRE, PROTEIN, FULLNESS}

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

        se();

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
        System.out.println(opskrift.toMaethedsAsciiTable());

    }

    @ShellMethod("gem")
    public void gem(String opskriftnavn) {

        Opskrift opskrift = new Opskrift(valgtMadMap.values().stream().toList());

        // Create a Jackson ObjectMapper with YAML support
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        // Persist to a YAML file
        try {
            File file = new File(opskriftnavn + ".yaml");
            mapper.writeValue(file, opskrift);
            System.out.println("Recipe saved to" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(5);
        }

        System.out.println(opskrift.toAsciiTable());

    }

    @ShellMethod("justertil")
    public void justertil(String antalKcal) {

        double onsketAntalKcal = 0;

        try {
            onsketAntalKcal = Double.parseDouble(antalKcal);
            System.out.println(String.format("Onsket antal kcal: %s", onsketAntalKcal));
        } catch (NumberFormatException e) {
            System.out.println(String.format("Det indtastede tal er ikke validt %s", onsketAntalKcal));
            return;
        }

        if (valgtMadMap.isEmpty()) {
            System.out.println("Der skal indtastes madvarer før man kan justere kalorier");
            return;
        }

        Opskrift opskrift = new Opskrift(valgtMadMap.values().stream().toList());
        double gammeltotalKcal = opskrift.getOpskriftTotalKcal();
        double justeringsfactor = (onsketAntalKcal/gammeltotalKcal);
        System.out.println(String.format("Justeringsfaktor er beregnet til %s", justeringsfactor));

        valgtMadMap.values().forEach(

                food -> {
                    System.out.println(String.format("%s gram før %s", food.getName(), food.getGram()));
                    food.setGram((int) (food.getGram() * justeringsfactor));
                    System.out.println(String.format("%s gram efter %s", food.getName(), food.getGram()));
                });

        se();

    }


    @ShellMethod("seopskrifter")
    public void seopskrifter() {
        String libraryPath = ".";

        try (Stream<Path> paths = Files.walk(Paths.get(libraryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yaml"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    @ShellMethod("hentopskrift")
    public void hentopskrift(String navn) {

        // Create a Jackson ObjectMapper with YAML support
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        // Persist to a YAML file
        try {
            File file = new File(navn + ".yaml");
            Opskrift opskrift = mapper.readValue(file, Opskrift.class);
            this.foods = opskrift.getValgtmad();
            System.out.println("Opskrift " + file.getAbsolutePath() + " læst.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(5);
        }
    }


    @ShellMethod("fullness")
    public String fullness() {


        Collections.sort(foods, (o1, o2) -> {
            return Double.compare(o2.getFullnessFactor(), o1.getFullnessFactor()); // Descending order
        });

        return foodsAsTable(MakroType.PROTEIN);
    }

    @ShellMethod("protein")
    public String protein() {


        Collections.sort(foods, (o1, o2) -> {
            return Double.compare(o2.getProteinIn100Gram(), o1.getProteinIn100Gram()); // Descending order
        });

        return foodsAsTable(MakroType.PROTEIN);
    }

    @ShellMethod("ny")
    public String ny() {
        Scanner scanner = new Scanner(System.in);
        Food food = new Food();


        System.out.println("Indtast navn");
        food.setName(scanner.nextLine());

        System.out.println("Indtast kcal pr 1000 gram");
        food.setKcalIn100Gram(Integer.parseInt(scanner.nextLine()));

        return food.toString();

    }

    @ShellMethod("fibre")
    public String fibre() {


        Collections.sort(foods, (o1, o2) -> {
            return Double.compare(o2.getDietaryfibreIn100gram(), o1.getDietaryfibreIn100gram()); // Descending order
        });

        return foodsAsTable(MakroType.FIBRE);
    }

    private String foodsAsTable(MakroType type) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("Id", "Navn", "Kalorier", "Protein (g)", "Kulhydrat (g)", "Fedt (g)", "Fiber (g)", "Fullness");
        table.addRule();

        for (Food food : foods) {
            if (MakroType.FIBRE == type && food.getDietaryfibreIn100gram() > 0 || MakroType.PROTEIN == type && food.getProteinIn100Gram() > 0) {
                table.addRow(food.getId(), food.getName(), food.getKcalIn100Gram(), food.getProteinIn100Gram(), food.getCarbonhydratesIn100Gram(), food.getFatIn100Gram(), food.getDietaryfibreIn100gram(), food.getFullnessFactor());
            } else {
                continue;
            }
            table.addRule();
        }
        table.getRenderer().setCWC(new CWC_LongestLine());
        return table.render();
    }
}
