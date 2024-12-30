package dk.brokso.foodaugust.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpskriftWriter {



    public static void writeOpskrift(Opskrift opskrift) {

        try {
            Files.write(Paths.get(opskrift.getNavn() + ".md"), OpskriftFormatter.formatOpskrift(opskrift));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        ;

    }

}
