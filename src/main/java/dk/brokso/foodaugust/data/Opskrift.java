package dk.brokso.foodaugust.data;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;

import java.util.List;

public class Opskrift {


    private final List<Food> valgtmad;
    private float opskriftTotalKcal = 0f;
    private float opskriftTotalProtein = 0f;
    private float opskriftTotalFat = 0f;
    private float opskriftTotalCarbonhydrates = 0f;
    private float opskriftTotalDietaryfibre = 0f;
    private float opskriftMakroNaeringlWeigt = 0f;
    private float opskriftGramValgt = 0f;
    private float opskriftPercentageProtein = 0f;
    private float opskriftPercentageCarbonhydrates = 0f;
    private float opskriftPercentageFat = 0f;
    private float opskriftPercentageDietaryfibre = 0f;

    public Opskrift(List<Food> valgtmad) {
        this.valgtmad = valgtmad;
        calculateTotals();
    }


    private void calculateTotals() {

        for (Food food : valgtmad) {
            opskriftTotalKcal = opskriftTotalKcal + food.getTotalCalories();
            opskriftTotalProtein = opskriftTotalProtein + food.getGramProtein();
            opskriftTotalFat = opskriftTotalFat + food.getGramFat();
            opskriftTotalCarbonhydrates = opskriftTotalCarbonhydrates + food.getGramCarbonhydrates();
            opskriftTotalDietaryfibre = opskriftTotalDietaryfibre + food.getGramDietaryfibre();
            opskriftGramValgt = opskriftGramValgt + food.getGram();
        }

        opskriftMakroNaeringlWeigt = opskriftTotalProtein + opskriftTotalFat + opskriftTotalCarbonhydrates;


        opskriftPercentageCarbonhydrates = opskriftTotalCarbonhydrates / opskriftMakroNaeringlWeigt * 100;
        opskriftPercentageProtein = opskriftTotalProtein / opskriftMakroNaeringlWeigt * 100;
        opskriftPercentageFat = opskriftTotalFat / opskriftMakroNaeringlWeigt * 100;


    }

    @Override
    public String toString() {
        return "Opskrift{" +
                "valgtmad=" + valgtmad +
                ", opskriftTotalKcal=" + opskriftTotalKcal +
                ", opskriftTotalProtein=" + opskriftTotalProtein +
                ", opskriftTotalFat=" + opskriftTotalFat +
                ", opskriftTotalCarbonhydrates=" + opskriftTotalCarbonhydrates +
                ", opskriftTotalDietaryfibre=" + opskriftTotalDietaryfibre +
                '}';
    }

    public String toAsciiTable() {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("Id", "Navn", "Valgt Vægt (g)", "Kalorier", "Protein (g)", "Kulhydrat (g)", "Fedt (g)", "Fiber (g)");
        table.addRule();

        for (Food food : valgtmad) {
            table.addRow(food.getId(), food.getName(), food.getGram(), food.getTotalCalories(), food.getGramProtein(), food.getGramCarbonhydrates(), food.getGramFat(), food.getGramDietaryfibre());
            table.addRule();
        }

        table.addRow("Ialt", "", this.opskriftGramValgt, this.opskriftTotalKcal, this.opskriftTotalProtein, this.opskriftTotalCarbonhydrates, this.opskriftTotalFat, this.opskriftTotalDietaryfibre);
        table.addRule();
        table.addRow("%", "", "", "", this.opskriftPercentageProtein, this.opskriftPercentageCarbonhydrates, this.opskriftPercentageFat, "");
        table.addRule();

        table.getRenderer().setCWC(new CWC_LongestLine());

        return table.render();
    }


}
