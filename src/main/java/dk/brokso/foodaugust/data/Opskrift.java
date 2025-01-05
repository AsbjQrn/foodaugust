package dk.brokso.foodaugust.data;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import dk.brokso.foodaugust.util.Calculator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Opskrift {


    private final List<Food> valgtmad;
    private String navn;
    private double opskriftTotalKcal = 0d;
    private double opskriftTotalProtein = 0d;
    private double opskriftTotalFat = 0d;
    private double opskriftTotalCarbonhydrates = 0d;
    private double opskriftTotalDietaryfibre = 0d;
    private double opskriftMakroNaeringlWeigt = 0d;
    private double opskriftTotalGramValgt = 0d;

    //  Disse procenter bliver regnet fra total mængde makronæringsstoffer og
    private double opskriftPercentageProtein = 0d;
    private double opskriftPercentageCarbonhydrates = 0d;
    private double opskriftPercentageFat = 0d;
    private double opskriftPercentageDietaryfibre = 0d;

    //  Bliver regnet fra opskriftens totalvægt
    private double opskriftKiloKalorierpr100Gr = 0d;
    private double opskriftProteinPr100Gr = 0d;
    private double opskriftCarbonhydratesPr100Gr = 0d;
    private double opskriftFatPr100Gr = 0d;
    private double opskriftDietaryfibrePr100Gr = 0d;

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

//      kiloKalorier pr 100
        opskriftKiloKalorierpr100Gr = opskriftTotalKcal/opskriftTotalGramValgt * 100;

//      protein pr 100 gram
        opskriftProteinPr100Gr = opskriftTotalProtein / opskriftTotalGramValgt * 100;

//      kulhydrat 100 gram
        opskriftCarbonhydratesPr100Gr = opskriftTotalCarbonhydrates / opskriftTotalGramValgt * 100;

//      fedt 100 gram
        opskriftFatPr100Gr = opskriftTotalFat / opskriftTotalGramValgt * 100;

//      fibre 100 gram
        opskriftDietaryfibrePr100Gr = opskriftTotalDietaryfibre / opskriftTotalGramValgt * 100;


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




   }
