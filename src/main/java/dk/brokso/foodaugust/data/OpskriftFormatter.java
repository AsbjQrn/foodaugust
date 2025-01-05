package dk.brokso.foodaugust.data;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import dk.brokso.foodaugust.util.Calculator;

import java.util.ArrayList;
import java.util.Collection;

public class OpskriftFormatter {


    private final static String TABELOVERSKRIFT =
            "| Id | Navn                                                    |Vægt (g)|Kalorier|Protein (g)|Kulhydrat (g)|Fedt (g)|Fiber (g)|Fullness|Kaloriedensitet kcal/gram|";

    private final static String TABELFORMATLINIE =
            "|:--:|:--------------------------------------------------------|:------:|:------:|:---------:|:-----------:|:------:|:-------:|:------:|:-----------------------:|";

    private final static String SKIL = "|";


    public static String toAsciiTableForConsole(Opskrift opskrift) {
        AsciiTable table = getAsciiTable(opskrift);

        return table.render();
    }

    public static Collection<String> toMarkdown(Opskrift opskrift) {
        Collection<String> markdown = new ArrayList<>();
        markdown.add(opskrift.getNavn());
        markdown.add("");
        markdown.add(TABELOVERSKRIFT);
        markdown.add(TABELFORMATLINIE);
        for (Food food : opskrift.getValgtmad()) {
            markdown.add(SKIL + food.getId() + SKIL + food.getName() + SKIL + (int) food.getGram() + SKIL + (int) food.getTotalCalories() + SKIL + (int) food.getGramProtein() + SKIL + (int) food.getGramCarbonhydrates() + SKIL + (int) food.getGramFat() + SKIL + (int) food.getGramDietaryfibre() + SKIL + getFormattedTwoDigits(food.getFullnessFactor()) + SKIL + getFormattedTwoDigits(food.getTotalCalories() / food.getGram()));
        }

        markdown.add("Ialt" + SKIL + "" + SKIL + opskrift.getOpskriftTotalGramValgt() + SKIL + opskrift.getOpskriftTotalKcal() + SKIL + getFormattedTwoDigits(opskrift.getOpskriftTotalProtein()) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftTotalCarbonhydrates()) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftTotalFat()) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftTotalDietaryfibre()) + SKIL + getFormattedTwoDigits(Calculator.calculateFullnessFactor(opskrift.getOpskriftKiloKalorierpr100Gr(), opskrift.getOpskriftProteinPr100Gr(), opskrift.getOpskriftDietaryfibrePr100Gr(), opskrift.getOpskriftFatPr100Gr())) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftTotalKcal() / opskrift.getOpskriftTotalGramValgt()));
        markdown.add("%" + SKIL + "" + SKIL + "" + SKIL + "" + SKIL + getFormattedTwoDigits(opskrift.getOpskriftPercentageProtein()) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftPercentageCarbonhydrates()) + SKIL + getFormattedTwoDigits(opskrift.getOpskriftPercentageFat()) + SKIL + "" + SKIL + "" + SKIL + "");
        markdown.add("");

        return markdown;
    }

    private static String getFormattedTwoDigits(Double kommaTal) {
        return String.format("%.2f", kommaTal);
    }

    private static AsciiTable getAsciiTable(Opskrift opskrift) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("Id", "Navn", "Valgt Vægt (g)", "Kalorier", "Protein (g)", "Kulhydrat (g)", "Fedt (g)", "Fiber (g)", "Fullness", "Kaloriedensitet kcal/gram");
        table.addRule();

        for (Food food : opskrift.getValgtmad()) {
            table.addRow(food.getId(), food.getName(), (int) food.getGram(), (int) food.getTotalCalories(), (int) food.getGramProtein(), (int) food.getGramCarbonhydrates(), (int) food.getGramFat(), (int) food.getGramDietaryfibre(), getFormattedTwoDigits(food.getFullnessFactor()), getFormattedTwoDigits(food.getTotalCalories() / food.getGram()));
            table.addRule();
        }

        table.addRow("Ialt", "", opskrift.getOpskriftTotalGramValgt(),(int) opskrift.getOpskriftTotalKcal(), (int) opskrift.getOpskriftTotalProtein(), (int) opskrift.getOpskriftTotalCarbonhydrates(), (int) opskrift.getOpskriftTotalFat(), (int) opskrift.getOpskriftTotalDietaryfibre(), getFormattedTwoDigits(Calculator.calculateFullnessFactor(opskrift.getOpskriftKiloKalorierpr100Gr(), opskrift.getOpskriftProteinPr100Gr(), opskrift.getOpskriftDietaryfibrePr100Gr(), opskrift.getOpskriftFatPr100Gr())), getFormattedTwoDigits(opskrift.getOpskriftTotalKcal() / opskrift.getOpskriftTotalGramValgt()));
        table.addRule();
        table.addRow("%", "", "", "", getFormattedTwoDigits(opskrift.getOpskriftPercentageProtein()), getFormattedTwoDigits(opskrift.getOpskriftPercentageCarbonhydrates()), getFormattedTwoDigits(opskrift.getOpskriftPercentageFat()), "", "", "");
        table.addRule();

        table.getRenderer().setCWC(new CWC_LongestLine());
        return table;
    }


}
