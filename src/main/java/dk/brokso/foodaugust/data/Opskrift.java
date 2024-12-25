package dk.brokso.foodaugust.data;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.Getter;

import java.util.List;

@Getter
public class Opskrift {


    private final List<Food> valgtmad;
    private float opskriftTotalKcal = 0f;
    private float opskriftTotalProtein = 0f;
    private float opskriftTotalFat = 0f;
    private float opskriftTotalCarbonhydrates = 0f;
    private float opskriftTotalDietaryfibre = 0f;
    private float opskriftMakroNaeringlWeigt = 0f;
    private float opskriftTotalGramValgt = 0f;
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
            opskriftTotalGramValgt = opskriftTotalGramValgt + food.getGram();
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

        table.addRow("Ialt", "", this.opskriftTotalGramValgt, this.opskriftTotalKcal, this.opskriftTotalProtein, this.opskriftTotalCarbonhydrates, this.opskriftTotalFat, this.opskriftTotalDietaryfibre);
        table.addRule();
        table.addRow("%", "", "", "", this.opskriftPercentageProtein, this.opskriftPercentageCarbonhydrates, this.opskriftPercentageFat, "");
        table.addRule();

        table.getRenderer().setCWC(new CWC_LongestLine());

        return table.render();
    }


    /**
     * Beregning af mæthedstal
     * <p>
     * <p>
     * 100 gram HAVREGRYN,  som indeholder: 366 kcal, 13 gram protein, 68 gram kulhydrat, 7 gram fedt, 10 gram fiber
     * mæthedstal = 366 kcal / 100 gram * (13 gram protein + (10 gram fiber * 2) - 7 gram fedt) = 95
     * <p>
     * <p>
     * <p>
     * 100 gram MØRK CHOKOLADE som indeholder: 549 kcal, 4,3 gram protein, 32 gram kulhydrat, 32 gram fedt, 1 gram fiber
     * mæthedstal = 549 kcal / 100 gram * (4  gram protein + (1 gram fiber * 2) - 32 gram fedt) = -143
     *
     * i opskriftformlen regnes der alid på 100 gram af opskriften - derfor divideres med 100 hist og pist
     */
    public String toMaethedsAsciiTable() {

        float kaloriedensitet = this.opskriftTotalKcal / this.opskriftTotalGramValgt;
        float justertil100gram = this.opskriftTotalGramValgt/100;
        float maethedstal = (this.opskriftTotalKcal / this.opskriftTotalGramValgt) * justertil100gram * (this.opskriftTotalProtein * justertil100gram + (this.opskriftTotalDietaryfibre * justertil100gram * 2) - this.opskriftTotalFat * justertil100gram);


        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("Kaloriedensitet kcal/gram", "slanketal");
        table.addRow(kaloriedensitet, maethedstal);//mæthedstal beregnes til at gælde 100 gra af opskriften - så det er altid er det samme uanset portionsstørrelse
        table.addRule();

        table.getRenderer().setCWC(new CWC_LongestLine());

        return table.render();

    }

}
