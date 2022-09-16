package animals;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    final static LocalTime now = LocalTime.now();

    final static LocalTime night = LocalTime.of(3,0);

    final static LocalTime morning = LocalTime.of(5,0);
    final static LocalTime evening = LocalTime.of(18,0);

    final static List<String > positiveAnswers = List.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct",
            "indeed", "you bet", "exactly", "you said it");

    final static List<String> negativeAnswers = List.of("n", "no", "no way", "nah", "nope", "negative", "i don't think so", "yeah no");

    final static List<String> clarificationQuestions = List.of("I'm not sure I caught you: was it yes or no?",
                                                                "Funny, I still don't understand, is it yes or no?",
                                                                "Oh, it's too complicated for me: just tell me yes or no.",
                                                                "Could you please simply say yes or no?",
                                                                "Oh, no, don't try to confuse me: say yes or no.");
    final static List<String> phrasesToSayGoodbye = List.of("Bye", "Goodbye", "See you later", "Have a nice day");
    static BinaryTree tree = new BinaryTree();
    static String newAnimal;
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
        enterAnimals();

    }

    private static void enterAnimals() {
        System.out.println("I want to learn about animals.");
        System.out.println("Which animal do you like most?");
        tree.root = new Node(addArticle(input()));
        playGame();

    }

    private static String input() {
        //create strings array and delete all articles before animal
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String addArticle(String nameOfAnimal) {
        //check first letter(vowel or not) in name of animal and add article by rules
        String animalWithArticle;
        if (!nameOfAnimal.equalsIgnoreCase("an unicorn") && !nameOfAnimal.equalsIgnoreCase("a unicorn")) {

            nameOfAnimal = nameOfAnimal.replaceAll("\\b(a|an|the)\\b\\s", "");

            if (nameOfAnimal.toLowerCase().matches("^[aeiyou].*\\b\\s?\\w*|^xeme\\s?\\w*")) {
                animalWithArticle = "an " + nameOfAnimal.toLowerCase();
            } else {
                animalWithArticle = "a " + nameOfAnimal.toLowerCase();
            }
        } else {
            animalWithArticle = nameOfAnimal.toLowerCase();
        }

        return animalWithArticle;

    }

    private static void playGame() {
        System.out.printf("Wonderful! I've learned so much about animals!\n" +
                "Let's play a game!\n" +
                "You think of an animal, and I guess it.%n");
        String line;
        do {
            System.out.println("Press enter when you're ready.");
            line = input();
        } while (!line.isBlank());

        if (tree.root.value.matches("(a|an)\\s\\w*")) {
            System.out.printf("Is it %s?%n", tree.root.value);
        } else {
            System.out.printf("%s?%n", tree.root.value.replaceFirst("it can", "Can it")
                    .replaceFirst("it has", "Does it have")
                    .replaceFirst("it is", "Is it"));
        }

        String confirmation = checkConfirmation();
        if ("no".equals(confirmation)) {
            System.out.println("I give up. What animal do you have in mind?");
            newAnimal = addArticle(input());
            specifyFacts();
        } else if ("yes".equals(confirmation)) {
            System.out.printf("Is it %s?%n", tree.root.right != null ? tree.root.right.value : tree.root.value);

            String confirmationNext = input().toLowerCase().strip();

            while (!isRightConfirmation(confirmationNext)) {
                askClarificationQuestion();
            }
        }

    }

    private static void specifyFacts() {
        System.out.printf("Specify a fact that distinguishes %s from %s.%n", tree.root.value, newAnimal);
        System.out.printf("The sentence should be of the format: %n" +
                "- It can ...%n" +
                "- It has ...%n" +
                "- It is a/an...%n");
        addFact(input());
    }

    private static void addFact(String fact) {

            if (isCorrectFact(fact)) {
                addFactToAnotherAnimal(fact);

            } else {
                System.out.println("The examples of a statement:");
                System.out.println("- It can fly");
                System.out.println("- It has horn");
                System.out.println("- It is a mammal");
                specifyFacts();
            }
    }

    private static boolean isCorrectFact(String fact) {
        Pattern pattern = Pattern.compile("^It\\s(can|has|is)\\s.*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fact);
        return matcher.matches();
    }

    private static void addFactToAnotherAnimal(String fact) {

        System.out.printf("Is the statement correct for %s?%n", newAnimal);
        String confirmation = checkConfirmation();
        if ("no".equals(confirmation)) {
            /*tree.root.right = new Node(fact);
            tree.root.right.right = tree.root;
            tree.root = tree.root.right;
            tree.root.left = new Node(newAnimal);*/

        } //else if (positiveAnswers.contains(confirmation.replaceAll("!\\.", ""))) {

        //}
        printResume(fact);
    }

    private static void printResume(String fact) {
        String positive = fact.replaceFirst("(It|it)\\s", "");
        String negative = positive.replaceFirst("\\bcan\\b", "can't")
                .replaceFirst("\\bhas\\b", "doesn't have")
                .replaceFirst("\\bis\\b", "isn't");
        System.out.println("I have learned the following facts about animals:");
        System.out.printf("- %s %s.%n", tree.root.right.value.replaceFirst("\\b(a|an)\\b", "The"), positive);
        System.out.printf("- %s %s.%n", tree.root.left.value.replaceFirst("\\b(a|an)\\b", "The"), negative);
        System.out.println("I can distinguish these animals by asking the question:");
        System.out.printf("- %s?%n", positive.replaceFirst("can", "Can it")
                .replaceFirst("has", "Does it have")
                .replaceFirst("is", "Is it"));
        System.out.println("Nice! I've learned so much about animals!");
        repeatGame();
    }

    private static void repeatGame() {
        System.out.println("Would you like to play again?");
        String confirmation = checkConfirmation();
        if ("no".equals(confirmation)) {
            sayGoodBye();
        } else if ("yes".equals(confirmation)) {
            playGame();
        }
    }

    private static String checkConfirmation() {
        String confirmation = input().toLowerCase();
        while (!isRightConfirmation(confirmation)) {
            askClarificationQuestion();
            confirmation = input().toLowerCase();
        }
        for (String answer : positiveAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                confirmation = "yes";
                break;
            }
        }
        for (String answer : negativeAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                confirmation = "no";
                break;
            }
        }
        return confirmation;
    }


    private static boolean isRightConfirmation(String confirmation) {
        boolean check = false;
        for (String answer : positiveAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
                check = true;
                break;
            }
        }
        for (String answer : negativeAnswers) {
            if (confirmation.matches(answer + "[!.]?")) {
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


