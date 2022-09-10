package animals;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    final static LocalTime now = LocalTime.now();

    final static LocalTime night = LocalTime.of(3,0);

    final static LocalTime morning = LocalTime.of(5,0);
    final static LocalTime evening = LocalTime.of(18,0);

    final static List<String > positiveAnswers = List.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct",
            "indeed", "you bet", "exactly", "you said it");

    final static List<String> negativeAnswers = List.of("n", "no", "no way", "nah", "nope", "negative", "I don't think so", "yeah no");



    public static void main(String[] args) {
        greetUser();
    }
    private static void greetUser() {
        if (now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(night)) {
            System.out.println("Hi, Night Owl");
        }else if (now.isAfter(night) && now.isBefore(morning)) {
            System.out.println("Hi, Early Bird");
        } else if (now.isAfter(morning) && now.isBefore(LocalTime.NOON)) {
            System.out.println("Good morning");
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(evening)) {
            System.out.println("Good afternoon");
        } else {
            System.out.println("Good evening");
        }
        enterAnimal();

    }

    private static void enterAnimal() {
        System.out.println("Enter an animal:");
        checkInput(input());
    }

    private static String input() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().strip().toLowerCase();
    }

    private static void checkInput(String input) {
        String[] animal = input.split("\\s+");
        String animalWithArticle = "";
        if (animal.length == 1 && animal[0].matches("\\b[aeiyou].*\\b")) {
            animalWithArticle = "an " + animal[0];
        } else {
            animalWithArticle = "a " + animal[0];
        }
        printQuestion(animalWithArticle);
    }

    private static void printQuestion(String animalWithArticle) {
        System.out.printf("Is it %s?%n", animalWithArticle);
        checkConfirmation(input());
    }

    private static void checkConfirmation(String confirmation) {
        boolean check = true;
        do {
            if (positiveAnswers.contains(confirmation)) {
                System.out.println("You answered: Yes");
                sayGoodBye();
            } else if (negativeAnswers.contains(confirmation)) {
                System.out.println("You answered: No");
                sayGoodBye();
            } else {
                System.out.println("Come on, yes or no?");
                check = false;
            }
        } while (!check);
    }

    private static void sayGoodBye() {
        System.out.println("bye");
    }


}


