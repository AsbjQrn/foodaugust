package dk.brokso.foodaugust.util;

public class Calculator {


    // Method to calculate the Fullness Factor (FF)
    public static double calculateFullnessFactor(double kiloKalorierpr100Gr, double proteinPr100Gr, double dietaryfibrePr100Gr, double fatPr100Gr) {
        // Calculate each part of the formula
        double term1 = 41.7 / Math.pow(kiloKalorierpr100Gr, 0.7);
        double term2 = 0.05 * proteinPr100Gr;
        double term3 = 6.17E-4 * Math.pow(dietaryfibrePr100Gr, 3); //6.17E-4 = 6.17 * 10 i minus 4 potens
        double term4 = -7.25E-6 * Math.pow(fatPr100Gr, 3);
        double term5 = 0.617;

        // Sum the terms
        double fullnessFactor = term1 + term2 + term3 + term4 + term5;

        // Apply the MIN and MAX functions to ensure FF is within the bounds of 0.5 and 5.0
        fullnessFactor = Math.max(0.5, Math.min(5.0, fullnessFactor));

        return fullnessFactor;
    }

}
