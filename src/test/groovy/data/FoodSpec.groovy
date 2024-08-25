package data

import dk.brokso.foodaugust.data.Food
import spock.lang.Specification
import spock.lang.Subject

class FoodSpec extends Specification {


    def 'Test udregning af mængder fra Food object  '(){
        given:

        @Subject
        Food food = new Food("Testfood", 600, 15, 25, 58, 2 )


        when:
        food.setGram(35)

        then:
        food.getTotalCalories() == 210f
        food.getGramProtein() == 5.25f
        food.getGramCarbonhydrates() == 20.3f
        food.getTotalFat() == 8.75f
        food.getTotaldietaryfibre() == 0.7f


        when:
        food.setGram(550)

        then:
        food.getTotalCalories() == 3300f
        food.getGramProtein() == 82.5f
        food.getGramCarbonhydrates() == 319f
        food.getTotalFat() == 137.5f
        food.getTotaldietaryfibre() == 11f



    }



}
