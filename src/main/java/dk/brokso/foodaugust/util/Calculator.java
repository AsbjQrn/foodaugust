package dk.brokso.foodaugust.util;

public class Calculator {


    public static double calculateFullnessFactor(double kiloKalorierpr100Gr, double proteinPr100Gr, double dietaryfibrePr100Gr, double fatPr100Gr) {
        double vaegt1 = 41.7 / Math.pow(kiloKalorierpr100Gr, 0.7);
        double vaegt2 = 0.05 * proteinPr100Gr;
        double vaegt3 = 6.17E-4 * Math.pow(dietaryfibrePr100Gr, 3);
        double vaegt4 = -7.25E-6 * Math.pow(fatPr100Gr, 3);
        double vaegt5 = 0.617;

        double fullnessFactor = vaegt1 + vaegt2 + vaegt3 + vaegt4 + vaegt5;

        // Apply the MIN and MAX functions to ensure FF is within the bounds of 0.5 and 5.0
        fullnessFactor = Math.max(0.5, Math.min(5.0, fullnessFactor));

        return fullnessFactor;
    }

}
