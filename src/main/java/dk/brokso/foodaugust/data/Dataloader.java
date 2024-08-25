package dk.brokso.foodaugust.data;


import dk.brokso.foodaugust.util.Loggable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Dataloader implements Loggable {


    @Bean
    public List<Food> readFile() {

        logger().info("Starter dataload");

        List<Food> foods = new ArrayList<>();

        try (InputStream inputStream = new ClassPathResource("madvarer.csv").getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line = "";
            line = fileReader.readLine();
            int i = 0;
            do {
                List<String> dataLine = splitLinie(line);
                if (!hasSkipCondition(dataLine)) {
                    foods.add(Food.of(dataLine));
                }
                i++;
                line = fileReader.readLine();
            } while ((line != null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger().info("Antal madvarer loaded: " + foods.size());
        logger().info("Afslutter dataload");
        return foods;
    }

    private static boolean hasSkipCondition(List<String> dataLine) {

        if (dataLine.get(0).trim().equals(""))
            return true;

        if (dataLine.get(1).trim().contains("FoodName"))
            return true;

        return false;
    }

    public List<Food> load() {

        logger().info("Starter dataload");

        List<Food> foods = new ArrayList<>();

        var fil = new File("");
        System.out.println("****************************************************************************" + fil.getAbsolutePath());


        try (BufferedReader fileReader = new BufferedReader(new FileReader("madvarer.csv"))) {

            String line = "";
            line = fileReader.readLine();
            int i = 0;
            do {
                List<String> dataLine = splitLinie(line);
                if (dataLine.get(5).equals("kcal/100 g")) continue;
                foods.add(Food.of(dataLine));
                i++;
                line = fileReader.readLine();
            } while ((line != null));


        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        return foods;

    }

    private static List<String> splitLinie(String line) {
        List<String> entries = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentEntry = new StringBuilder();

        for (char ch : line.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {

                String current = currentEntry.toString().trim();

                if (isNumberWihComma(current.toCharArray())) {
                    current = current.replace(',', '.');
                }

                entries.add(current);
                currentEntry.setLength(0); // Reset the builder
            } else {
                currentEntry.append(ch);
            }
        }

        // Add the last entry
        entries.add(currentEntry.toString().trim());

        return entries;
    }

    private static boolean isNumberWihComma(char[] chars) {

        int commas = 0;
        int numbers = 0;
        Set<Integer> ints = new HashSet<>();
        for (char ch : chars) {
            if (ch == ',') commas++;
            if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9')
                numbers++;
        }

        return chars.length == commas + numbers;


    }


}
