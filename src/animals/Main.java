package animals;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main {

    final static LocalTime now = LocalTime.now();

    final static LocalTime night = LocalTime.of(3,0);

    final static LocalTime morning = LocalTime.of(5,0);
    final static LocalTime evening = LocalTime.of(18,0);

    final static List<String > positiveAnswers = List.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct",
            "indeed", "you bet", "exactly", "you said it");

    final static List<String> negativeAnswers = List.of("n", "no", "no way", "nah", "nope", "negative", "I don't think so", "yeah no");

    final static List<String> clarificationQuestions = List.of("I'm not sure I caught you: was it yes or no?",
                                                                "Funny, I still don't understand, is it yes or no?",
                                                                "Oh, it's too complicated for me: just tell me yes or no.",
                                                                "Could you please simply say yes or no?",
                                                                "Oh, no, don't try to confuse me: say yes or no.");

    final static List<String> phrasesToSayGoodbye = List.of("Bye", "Goodbye", "See you later", "Have a nice day");

    public static void main(String[] args) {
        greetUser();
    }
    private static void greetUser() {
        // depend on local time say greetings
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
        //create strings array and delete all articles before animal
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toLowerCase().replaceAll("(\\ba |\\ban |\\bthe )", "").strip().toLowerCase();
    }

    private static void checkInput(String input) {
        //check first letter(vowel or not) in name of animal and add article by rules
        String[] animal = input.split("\\s+");
        String animalWithArticle;
        if (animal[0].matches("\\b[aeiyo].*\\b|xeme")) {
            animalWithArticle = "an " + Arrays.toString(animal).replaceAll("[,\\]\\[]","");
        } else {
            animalWithArticle = "a " + Arrays.toString(animal).replaceAll("[,\\]\\[]","");
        }
        printQuestion(animalWithArticle);
    }

    private static void printQuestion(String animalWithArticle) {
        System.out.printf("Is it %s?%n", animalWithArticle);
        while (!isRightConfirmation(input())) {
            askClarificationQuestion();
        }
        sayGoodBye();
    }

    private static boolean isRightConfirmation(String confirmation) {
        boolean check = false;
        for (String answer : positiveAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                System.out.println("You answered: Yes");
                check = true;
                break;
            }
        }
        for (String answer : negativeAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                System.out.println("You answered: No");
                check = true;
                break;
            }
        }

        return check;
    }

    private static void askClarificationQuestion() {
        System.out.println(chooseClarificationQuestion());
    }

    private static String chooseClarificationQuestion() {
        Random random = new Random();
        int indexOfQuestion = random.nextInt(clarificationQuestions.size());
        return clarificationQuestions.get(indexOfQuestion);
    }

    private static void sayGoodBye() {
        System.out.println(chooseHowSayGoodbye());
    }

    private static String chooseHowSayGoodbye() {
        Random random = new Random();
        int indexOfPhrase = random.nextInt(phrasesToSayGoodbye.size());
        return phrasesToSayGoodbye.get(indexOfPhrase);
    }


}


