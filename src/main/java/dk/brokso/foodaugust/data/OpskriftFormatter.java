package dk.brokso.foodaugust.data;

import java.util.ArrayList;
import java.util.List;

public class OpskriftFormatter {


    private final static String TABELOVERSKRIFT =
            "|Navn                                                    |    Gram    |    Id    | ";

    private final static String TABELFORMATLINIE =
            "|:-------------------------------------------------------|:----------:|:--------:| ";

    private final static String CELLEADSKIL = "|";

    public static List<String> formatOpskrift(Opskrift opskrift) {

        List<String> textlinier = new ArrayList<>();


        textlinier.add("# Navn: " + opskrift.getNavn());
        textlinier.add(TABELOVERSKRIFT);
        textlinier.add(TABELFORMATLINIE);

        for (Food food : opskrift.getValgtmad()) {
           String linie = CELLEADSKIL + food.getName() + CELLEADSKIL + (int) food.getGram() + CELLEADSKIL + food.getId();
           textlinier.add(linie);
        }

        return textlinier;

    }


}
